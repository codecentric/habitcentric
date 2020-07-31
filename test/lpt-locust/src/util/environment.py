import os


class Environment:

    @staticmethod
    def oidc_enabled():
        oidc_enabled = os.environ.get('OIDC_ENABLED')
        return oidc_enabled == 'true'

    @staticmethod
    def oidc_params():
        token_url = os.environ.get('OIDC_TOKEN_URL')
        client_id = os.environ.get('OIDC_CLIENT_ID')
        username = os.environ.get('OIDC_USERNAME')
        password = os.environ.get('OIDC_PASSWORD')

        return token_url, client_id, username, password

    @staticmethod
    def k8s_env():
        env = os.environ.get('ENV')
        return env == 'k8s'
