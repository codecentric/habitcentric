from locust import task, between

from helper import NoAuthRebuildHttpUser


class HabitcentricUser(NoAuthRebuildHttpUser):
    wait_time = between(2, 4)

    @task
    def index_page(self):
        json_web_token_with_user_id = \
            (
                'eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJGSjg2R2NGM2pUYk5MT2NvNE52WmtVQ0'
                'lVbWZZQ3FvcXRPUWVNZmJoTmxFIn0.eyJqdGkiOiI1YzVlMzUxOS03MjMwLTQwYzktYTNjYy00OTM1NmM4'
                'Mjk2YWYiLCJleHAiOjE1OTMxNjg5MjEsIm5iZiI6MCwiaWF0IjoxNTkzMTY4ODAxLCJpc3MiOiJodHRwcz'
                'ovL2hhYml0Y2VudHJpYy5kZW1vL2F1dGgvcmVhbG1zL2hhYml0Y2VudHJpYyIsImF1ZCI6InJlYWxtLW1h'
                'bmFnZW1lbnQiLCJzdWIiOiJlZjY5MmMwZS1iNjJmLTQ3MjItODE0Zi05ZmNiYjNhNjdhYmUiLCJ0eXAiOi'
                'JCZWFyZXIiLCJhenAiOiJ1aSIsImF1dGhfdGltZSI6MTU5MzE2NDE3NSwic2Vzc2lvbl9zdGF0ZSI6IjM5'
                'YzcxMjRmLTczMWQtNGI3ZS1iMzQwLTE2Y2IzNWZjZDI5NiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbn'
                'MiOlsiaHR0cHM6Ly9oYWJpdGNlbnRyaWMuZGVtbyIsImh0dHA6Ly9oYWJpdGNlbnRyaWMuZGVtbyJdLCJy'
                'ZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiYWRtaW4iLCJ1c2VyIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnsicm'
                'VhbG0tbWFuYWdlbWVudCI6eyJyb2xlcyI6WyJ2aWV3LWlkZW50aXR5LXByb3ZpZGVycyIsInZpZXctcmVh'
                'bG0iLCJtYW5hZ2UtaWRlbnRpdHktcHJvdmlkZXJzIiwiaW1wZXJzb25hdGlvbiIsInJlYWxtLWFkbWluIi'
                'wiY3JlYXRlLWNsaWVudCIsIm1hbmFnZS11c2VycyIsInF1ZXJ5LXJlYWxtcyIsInZpZXctYXV0aG9yaXph'
                'dGlvbiIsInF1ZXJ5LWNsaWVudHMiLCJxdWVyeS11c2VycyIsIm1hbmFnZS1ldmVudHMiLCJtYW5hZ2Utcm'
                'VhbG0iLCJ2aWV3LWV2ZW50cyIsInZpZXctdXNlcnMiLCJ2aWV3LWNsaWVudHMiLCJtYW5hZ2UtYXV0aG9y'
                'aXphdGlvbiIsIm1hbmFnZS1jbGllbnRzIiwicXVlcnktZ3JvdXBzIl19fSwic2NvcGUiOiJvcGVuaWQgZW'
                '1haWwgcHJvZmlsZSBhZGRyZXNzIiwiYWRkcmVzcyI6e30sImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwibmFt'
                'ZSI6IkRlZmF1bHQgRGVmYXVsdCIsInByZWZlcnJlZF91c2VybmFtZSI6ImRlZmF1bHQiLCJnaXZlbl9uYW'
                '1lIjoiRGVmYXVsdCIsImZhbWlseV9uYW1lIjoiRGVmYXVsdCIsImVtYWlsIjoiZGVmYXVsdEBoYWJpdGNl'
                'bnRyaWMuZGUifQ.MQPf0byYTZGVzhpy0kM9R14Ozb4s7WWQfD20Hyv1L0vLcG5bGVM0AhQRiWQJZJtodzq'
                '31RuBhpmOreWE7hpSQ3mgrZ-OI-BFamf02cxsatDHtwr3k5PJnbdVQx-TbONwB8qMWapHS3W-erJgHQLkA'
                'xQeXa8a9KNxvKQH_RliaPo'
            )

        headers = {
            'Authorization': 'Bearer ' + json_web_token_with_user_id
        }

        self.client.get(url='/habits', headers=headers, verify=False)
        self.client.get(url='/habits', headers=headers, verify=False)
        self.client.get(url='/habits', headers=headers, verify=False)
