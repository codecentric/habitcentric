import os

from locust import HttpUser
from locust.clients import HttpSession
from requests.adapters import HTTPAdapter

from k8s.ingressresolver import K8sIngressIpResolver


class NoRebuildAuthSession(HttpSession):
    # prevent stripping Authorization header on redirect with different host
    def rebuild_auth(self, prepared_request, response):
        pass


class NoAuthRebuildHttpUser(HttpUser):
    abstract = True

    def on_start(self):
        # prevent DNS request during proxy rebuild of redirect
        os.environ['NO_PROXY'] = 'habitcentric.demo'

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
            host_header_ssl_adapter = HostHeaderSSLAdapter(K8sIngressIpResolver())
            session.mount('http://', host_header_ssl_adapter)
            session.mount('https://', host_header_ssl_adapter)

        self.client = session


class HostHeaderSSLAdapter(HTTPAdapter):

    def __init__(self, ip_resolver: K8sIngressIpResolver):
        super().__init__()
        self.ip_resolver = ip_resolver

    def send(self, request, **kwargs):
        from urllib.parse import urlparse

        connection_pool_kwargs = self.poolmanager.connection_pool_kw

        result = urlparse(request.url)
        resolved_ip = self.ip_resolver.resolve()

        if resolved_ip:
            request.url = request.url.replace(
                result.hostname,
                resolved_ip,
            )

            if result.scheme == 'https':
                connection_pool_kwargs['server_hostname'] = result.hostname  # SNI
                connection_pool_kwargs['assert_hostname'] = result.hostname
            else:
                connection_pool_kwargs.pop('server_hostname', None)
                connection_pool_kwargs.pop('assert_hostname', None)

            # overwrite the host header
            request.headers['Host'] = result.hostname

        return super(HostHeaderSSLAdapter, self).send(request, **kwargs)



