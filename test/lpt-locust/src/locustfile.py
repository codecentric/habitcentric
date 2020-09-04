from locust import task, between

from helper import OidcHttpUser


class HabitcentricUser(OidcHttpUser):
    wait_time = between(2, 4)

    @task
    def index_page(self):
        habit = {
            'name': 'Test',
            'schedule': {
                'repetitions': 1,
                'frequency': 'DAILY'
            }
        }
        create_response = self.client.post(url='/habits', json=habit, verify=False)
        created_habit_id = create_response.headers['location'].split('/')[-1]

        self.client.put('/track/habits/{}'.format(created_habit_id), name='/track/habits/[id]', json=['2020-09-10', '2020-09-09', '2020-09-08'])

        self.client.get(url='/habits', verify=False)
        self.client.get(url='/report/achievement', verify=False)

        self.client.put('/track/habits/{}'.format(created_habit_id), name='/track/habits/[id]', json=[])
        self.client.delete('/habits/{}'.format(created_habit_id), name='/habits/[id]')
