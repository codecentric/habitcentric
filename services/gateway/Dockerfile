FROM openjdk:11.0.15-jre-slim-buster@sha256:d96b1affdca115786f193e6b420c11699f5fde3df730ceac128d840a2aa51073
WORKDIR /opt/
COPY build/libs/gateway-*.jar app.jar
EXPOSE 9000
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
