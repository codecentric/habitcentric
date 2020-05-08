FROM openjdk:11.0.7-jre-slim-buster@sha256:678022f7c59cae7d1afddfc800356611bdedf1030d94cfc503a60a0757d97b79
WORKDIR /opt/
COPY build/libs/report-*.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
