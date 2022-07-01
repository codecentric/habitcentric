FROM openjdk:11.0.15-jre-slim-buster@sha256:b744cc7f7856d7d6a37633e5501b2938aa16e3a16218170cec691d2a8c17b0ca
WORKDIR /opt/
COPY build/libs/report-*.jar app.jar
EXPOSE 9003
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
