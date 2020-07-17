from requests.adapters import HTTPAdapter

from k8s import K8sIngressIpResolver


class K8sDnsResolveAdapter(HTTPAdapter):

    def __init__(self, ip_resolver: K8sIngressIpResolver):
        super().__init__()
        self.ip_resolver = ip_resolver

    def send(self, request, **kwargs):
        from urllib.parse import urlparse

        connection_pool_kwargs = self.poolmanager.connection_pool_kw

        result = urlparse(request.url)
        resolved_ip = self.ip_resolver.resolve()
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

        return super(K8sDnsResolveAdapter, self).send(request, **kwargs)