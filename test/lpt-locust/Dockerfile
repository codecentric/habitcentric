FROM locustio/locust:2.15.1
RUN pip3 install kubernetes
COPY src /locust-config
ENV LOCUST_LOCUSTFILE /locust-config/locustfile.py
