FROM jboss/keycloak:16.1.0
ENV KEYCLOAK_IMPORT /tmp/habitcentric-realm.json
ADD realms/habitcentric-realm.json /tmp
COPY ./themes/codecentric /opt/jboss/keycloak/themes/codecentric
