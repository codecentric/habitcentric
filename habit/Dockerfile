FROM openjdk:11-jre-slim
WORKDIR /opt/
COPY build/libs/hc-habit-*.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
