from locust import task, between

from habit import generate_habit, generate_track_dates
from helper import OidcHttpUser


class HabitcentricUser(OidcHttpUser):
    wait_time = between(2, 4)

    @task
    def index_page(self):
        create_response = self.client.post(url='/habits', json=generate_habit(), verify=False)
        created_habit_id = create_response.headers['location'].split('/')[-1]

        self.client.put('/track/habits/{}'.format(created_habit_id), name='/track/habits/[id]', json=generate_track_dates())

        self.client.get(url='/habits', verify=False)
        self.client.get(url='/report/achievement', verify=False)

        self.client.put('/track/habits/{}'.format(created_habit_id), name='/track/habits/[id]', json=[])
        self.client.delete('/habits/{}'.format(created_habit_id), name='/habits/[id]')
