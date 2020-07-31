import os

from locust import HttpUser
from locust.clients import HttpSession

from k8s import K8sIngressIpResolver, K8sDnsResolveMiddleware
from oauth import OAuthMiddleware
from util import MiddlewareAdapter


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
        if self.oidc_enabled():
            token_url, client_id, username, password = self.load_oidc_from_env()
            middlewares.append(OAuthMiddleware(token_url=token_url, client_id=client_id, username=username, password=password))

        if self.k8s_env():
            middlewares.append(K8sDnsResolveMiddleware(K8sIngressIpResolver()))

        chain_adapter = MiddlewareAdapter(middlewares)
        session.mount('http://habitcentric.demo', chain_adapter)
        session.mount('https://habitcentric.demo', chain_adapter)
        self.client = session

    def oidc_enabled(self):
        oidc_enabled = os.environ.get('OIDC_ENABLED')
        return oidc_enabled == 'true'

    def load_oidc_from_env(self):
        token_url = os.environ.get('OIDC_TOKEN_URL')
        client_id = os.environ.get('OIDC_CLIENT_ID')
        username = os.environ.get('OIDC_USERNAME')
        password = os.environ.get('OIDC_PASSWORD')

        return token_url, client_id, username, password

    def k8s_env(self):
        env = os.environ.get('ENV')
        return env == 'k8s'
