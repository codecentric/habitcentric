FROM locustio/locust:2.32.5
COPY src /locust-config
RUN pip3 install -r /locust-config/requirements.txt
ENV LOCUST_LOCUSTFILE /locust-config/locustfile.py
