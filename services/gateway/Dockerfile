FROM azul/zulu-openjdk:21-jre-latest
WORKDIR /opt/
COPY build/libs/gateway-*.jar app.jar
EXPOSE 9000
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
