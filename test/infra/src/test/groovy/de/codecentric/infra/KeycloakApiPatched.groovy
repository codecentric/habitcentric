package de.codecentric.infra

import com.github.scribejava.apis.KeycloakApi
import com.github.scribejava.core.oauth2.clientauthentication.ClientAuthentication
import com.github.scribejava.core.oauth2.clientauthentication.RequestBodyAuthenticationScheme

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class KeycloakApiPatched extends KeycloakApi {
    private static final ConcurrentMap<String, KeycloakApiPatched> INSTANCES = new ConcurrentHashMap<>();

    protected KeycloakApiPatched(String baseUrlWithRealm) {
        super(baseUrlWithRealm)
    }

    static KeycloakApiPatched instance() {
        return instance("http://localhost:8080/", "master");
    }

    static KeycloakApiPatched instance(String baseUrl, String realm) {
        final String defaultBaseUrlWithRealm = composeBaseUrlWithRealm(baseUrl, realm);

        //java8: switch to ConcurrentMap::computeIfAbsent
        KeycloakApi api = INSTANCES.get(defaultBaseUrlWithRealm);
        if (api == null) {
            api = new KeycloakApiPatched(defaultBaseUrlWithRealm);
            final KeycloakApi alreadyCreatedApi = INSTANCES.putIfAbsent(defaultBaseUrlWithRealm, api);
            if (alreadyCreatedApi != null) {
                return alreadyCreatedApi;
            }
        }
        return api;
    }

    @Override
    ClientAuthentication getClientAuthentication() {
        return RequestBodyAuthenticationScheme.instance();
    }
}
