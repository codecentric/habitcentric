from oauthlib.oauth2 import LegacyApplicationClient
from requests_oauthlib import OAuth2Session

from util import BaseMiddleware


class OAuthMiddleware(BaseMiddleware):
    access_token: str

    def __init__(self, token_url: str, client_id: str, username: str, password: str):
        client = LegacyApplicationClient(client_id)
        oauth = OAuth2Session(client=client)
        token_response = oauth.fetch_token(token_url, username=username, password=password,
                                           verify=False)
        self.access_token = token_response['access_token']

    def before_send(self, request, poolmanager, *args, **kwargs):
        request.headers['Authorization'] = 'Bearer %s' % self.access_token
        super().before_send(request, poolmanager, *args, **kwargs)
