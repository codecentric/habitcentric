FROM openjdk:11.0.7-jre-slim-buster@sha256:7c9e7cef4155193f01c62af940eed2e0821cbfb173525bc74b869e3444b0771b
WORKDIR /opt/
COPY build/libs/report-*.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
