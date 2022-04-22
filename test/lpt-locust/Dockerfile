FROM locustio/locust:2.8.4
RUN pip3 install kubernetes
COPY src /locust-config
ENV LOCUST_LOCUSTFILE /locust-config/locustfile.py
