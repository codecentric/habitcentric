FROM locustio/locust:2.24.1
COPY src /locust-config
RUN pip3 install -r /locust-config/requirements.txt
ENV LOCUST_LOCUSTFILE /locust-config/locustfile.py
