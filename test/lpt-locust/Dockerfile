FROM locustio/locust:1.0.3
RUN pip3 install kubernetes
COPY src /locust-config
ENV LOCUST_LOCUSTFILE /locust-config/locustfile.py
