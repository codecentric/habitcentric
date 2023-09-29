FROM azul/zulu-openjdk:21-jre-latest
WORKDIR /opt/
COPY build/libs/habit-*.jar app.jar
EXPOSE 9001
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
