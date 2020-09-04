from typing import List

from requests.adapters import HTTPAdapter

from util import BaseMiddleware


class MiddlewareAdapter(HTTPAdapter):
    middlewares: List[BaseMiddleware]

    def __init__(self, middlewares: List[BaseMiddleware]):
        super(MiddlewareAdapter, self).__init__()
        self.middlewares = middlewares

    def send(self, request, **kwargs):
        for middleware in self.middlewares:
            middleware.before_send(request, self.poolmanager, **kwargs)

        return super(MiddlewareAdapter, self).send(request, **kwargs)