FROM jboss/keycloak:6.0.1
ENV KEYCLOAK_IMPORT /tmp/habitcentric-realm.json
ADD realms/habitcentric-realm.json /tmp
COPY ./themes/codecentric /opt/jboss/keycloak/themes/codecentric