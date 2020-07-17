from requests import PreparedRequest


class BaseMiddleware(object):
    def before_send(self, request: PreparedRequest, poolmanager, *args, **kwargs):
        """
        Called before `HTTPAdapter::send`.

        :param request: The `PreparedRequest` used to generate the response.
        :return: None
        """
        pass
