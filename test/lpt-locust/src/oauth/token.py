from typing import Any
from time import time


class OAuthToken(object):
    access_token: str
    refresh_token: str
    expires_at: float

    def __init__(self, token: Any):
        self.access_token = token['access_token']
        self.refresh_token = token['refresh_token']
        self.expires_at = token['expires_at']

    def is_expired(self) -> bool:
        return self.expires_at <= time()
