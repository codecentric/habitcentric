import os
from locust import HttpUser
from locust.clients import HttpSession
from requests.adapters import HTTPAdapter


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
        session.mount('http://', HostHeaderSSLAdapter())
        session.mount('https://', HostHeaderSSLAdapter())
        self.client = session


class HostHeaderSSLAdapter(HTTPAdapter):
    def resolve(self, hostname):
        # a dummy DNS resolver
        ip = '10.104.251.9'
        resolutions = {
            'habitcentric.demo': ip
        }
        return resolutions.get(hostname)

    def send(self, request, **kwargs):
        from urllib.parse import urlparse

        connection_pool_kwargs = self.poolmanager.connection_pool_kw

        result = urlparse(request.url)
        resolved_ip = self.resolve(result.hostname)

        if resolved_ip:
            request.url = request.url.replace(
                result.hostname,
                resolved_ip,
            )

            if result.scheme == 'https':
                connection_pool_kwargs['server_hostname'] = result.hostname  # SNI
                connection_pool_kwargs['assert_hostname'] = result.hostname


            # overwrite the host header
            request.headers['Host'] = result.hostname
        else:
            # theses headers from a previous request may have been left
            connection_pool_kwargs.pop('server_hostname', None)
            connection_pool_kwargs.pop('assert_hostname', None)

        return super(HostHeaderSSLAdapter, self).send(request, **kwargs)