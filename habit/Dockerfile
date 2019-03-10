FROM openjdk:8-jre-alpine
WORKDIR /opt/

COPY build/libs/hc-habit-*.jar app.jar

EXPOSE 8080

CMD [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
