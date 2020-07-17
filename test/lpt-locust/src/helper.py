import os

from locust import HttpUser
from locust.clients import HttpSession

from k8s import K8sIngressIpResolver, K8sDnsResolveAdapter


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

        env = os.environ.get('ENV')
        if env == 'k8s':
            k8s_dns_resolve_adapter = K8sDnsResolveAdapter(K8sIngressIpResolver())
            session.mount('http://habitcentric.demo', k8s_dns_resolve_adapter)
            session.mount('https://habitcentric.demo', k8s_dns_resolve_adapter)

        self.client = session
