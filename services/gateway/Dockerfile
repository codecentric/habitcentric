FROM openjdk:11.0.4-jre-slim-buster@sha256:a580951e71213718ff03f89b9a2bc9bd886fcd695dc7a548f791154e84e662ca
WORKDIR /opt/
COPY build/libs/gateway-*.jar app.jar
EXPOSE 9000
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
