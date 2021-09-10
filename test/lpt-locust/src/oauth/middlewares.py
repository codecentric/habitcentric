from oauthlib.oauth2 import LegacyApplicationClient
from requests_oauthlib import OAuth2Session

from oauth.token import OAuthToken
from util import BaseMiddleware


class OAuthMiddleware(BaseMiddleware):
    client_id: str
    token_url: str
    token: OAuthToken

    def __init__(self, token_url: str, client_id: str, username: str, password: str):
        self.client_id = client_id
        self.token_url = token_url
        self.fetch_token(username, password)

    def fetch_token(self, username: str, password: str):
        client = LegacyApplicationClient(self.client_id)
        oauth = OAuth2Session(client=client)
        token_response = oauth.fetch_token(self.token_url, username=username, password=password,
                                           verify=False)
        self.token = OAuthToken(token_response)

    def refresh_token(self):
        refresh_kwargs = {
            'client_id': self.client_id
        }
        oauth = OAuth2Session(auto_refresh_kwargs=refresh_kwargs)
        token_response = oauth.refresh_token(self.token_url, self.token.refresh_token, verify=False, kwargs={'client_id': self.client_id})
        self.token = OAuthToken(token_response)

    def before_send(self, request, poolmanager, *args, **kwargs):
        if self.token.is_expired():
            self.refresh_token()

        request.headers['Authorization'] = 'Bearer %s' % self.token.access_token
        super().before_send(request, poolmanager, *args, **kwargs)
