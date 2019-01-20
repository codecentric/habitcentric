FROM openjdk:8-jre-alpine
WORKDIR /opt/

COPY build/libs/hc-habit-*.jar app.jar

ENV PORT 8180
EXPOSE 8180

CMD [ "sh", "-c", "java $JAVA_OPTS -Dserver.port=$PORT -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
