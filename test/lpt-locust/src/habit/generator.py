from datetime import datetime, timedelta, date
import uuid
import random

from time import strftime

REPETITIONS_MAX = 10
FREQUENCIES = ['DAILY', 'WEEKLY', 'MONTHLY']


def generate_habit():
    random_name = str(uuid.uuid4())
    random_repetitions = random.randrange(start = 1, stop=REPETITIONS_MAX + 1)
    random_frequency = random.choice(FREQUENCIES)

    return {
        'name': random_name,
        'schedule': {
            'repetitions': random_repetitions,
            'frequency': random_frequency
        }
    }


def generate_track_dates():
    today = date.today()
    track_dates = [(today - timedelta(days)).strftime('%Y-%m-%d') for days in [1, 2, 3]]
    return track_dates