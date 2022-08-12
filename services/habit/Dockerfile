FROM openjdk:11.0.16-jre-slim-buster@sha256:d030d27043616146d95daafc3a3b588cbfd43311cf8a164177c949cad4f2dab3
WORKDIR /opt/
COPY build/libs/habit-*.jar app.jar
EXPOSE 9001
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
