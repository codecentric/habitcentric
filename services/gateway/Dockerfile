FROM openjdk:11.0.4-jre-slim-buster@sha256:1e4f3107324df951ae0adfd4f7018e8310104117b2ba9b9c495dcd3c30fff794
WORKDIR /opt/
COPY build/libs/gateway-*.jar app.jar
EXPOSE 9000
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
