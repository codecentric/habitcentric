import os

from locust import HttpUser
from locust.clients import HttpSession

from k8s import K8sIngressIpResolver, K8sDnsResolveMiddleware
from oauth import OAuthMiddleware
from util import MiddlewareAdapter, Environment


class NoRebuildAuthSession(HttpSession):
    # prevent stripping Authorization header on redirect with different host
    def rebuild_auth(self, prepared_request, response):
        pass


class NoAuthRebuildHttpUser(HttpUser):
    abstract = True

    def on_start(self):
        # prevent DNS request during proxy rebuild of redirect
        os.environ['NO_PROXY'] = self.environment.host

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        session = NoRebuildAuthSession(
            base_url=self.host,
            request_success=self.environment.events.request_success,
            request_failure=self.environment.events.request_failure,
        )
        session.trust_env = False

        middlewares = []
        if Environment.oidc_enabled():
            self.add_oidc_middleware(middlewares)

        if Environment.k8s_env():
            self.add_k8s_middleware(middlewares)

        self.mount_middlewares(middlewares, session)
        self.client = session

    def add_k8s_middleware(self, middlewares):
        middlewares.append(K8sDnsResolveMiddleware(K8sIngressIpResolver()))

    def add_oidc_middleware(self, middlewares):
        token_url, client_id, username, password = Environment.oidc_params()
        middlewares.append(
            OAuthMiddleware(token_url=token_url, client_id=client_id, username=username,
                            password=password))

    def mount_middlewares(self, middlewares, session):
        chain_adapter = MiddlewareAdapter(middlewares)
        session.mount('http://habitcentric.demo', chain_adapter)
        session.mount('https://habitcentric.demo', chain_adapter)
