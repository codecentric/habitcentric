FROM eclipse-temurin:17.0.6_10-jre
WORKDIR /opt/
COPY build/libs/habit-*.jar app.jar
EXPOSE 9001
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
