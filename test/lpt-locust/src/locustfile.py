from locust import task, between

from helper import OidcHttpUser


class HabitcentricUser(OidcHttpUser):
    wait_time = between(2, 4)

    @task
    def index_page(self):
        self.client.get(url='/habits', verify=False)
        self.client.get(url='/report/achievement', verify=False)
