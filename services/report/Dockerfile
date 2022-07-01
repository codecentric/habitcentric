FROM openjdk:11.0.7-jre-slim-buster@sha256:afd1c9c9138dc4bfe062f15ce74b2d0d9518d8f6f8309881e8f821cb5b182bf0
WORKDIR /opt/
COPY build/libs/report-*.jar app.jar
EXPOSE 9003
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
