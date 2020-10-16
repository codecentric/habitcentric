import logging

from locust import HttpUser

from oauth import OAuthMiddleware
from util import MiddlewareAdapter, Environment


class OidcHttpUser(HttpUser):
    abstract = True

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)

        middlewares = []
        if Environment.oidc_enabled():
            logging.info('Enabling OIDC for user')
            self.add_oidc_middleware(middlewares)

        self.mount_middlewares(middlewares, self.client)

    def add_oidc_middleware(self, middlewares):
        token_url, client_id, username, password =\
            Environment.oidc_params(self.host)
        middlewares.append(
            OAuthMiddleware(token_url=token_url, client_id=client_id,
                            username=username, password=password))

    def mount_middlewares(self, middlewares, session):
        chain_adapter = MiddlewareAdapter(middlewares)
        session.mount('http://habitcentric.demo', chain_adapter)
        session.mount('https://habitcentric.demo', chain_adapter)
