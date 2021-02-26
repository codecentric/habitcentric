package de.codecentric.hc.infra

import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.oauth.OAuth20Service
import com.github.scribejava.httpclient.okhttp.OkHttpHttpClient
import groovy.json.JsonSlurper
import groovy.transform.Immutable
import okhttp3.*

class RequestHelper {
    private OkHttpClient client;

    RequestHelper() {
        client = new OkHttpClient().newBuilder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build()

    }

    ResponseDto get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (def response = client.newCall(request).execute()) {
            new ResponseDto(response.code(), response.body().string())
        }
    }

    ResponseDto getWithAuth(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer ${getAccessToken()}")
                .build();
        try (def response = client.newCall(request).execute()) {
            new ResponseDto(response.code(), response.body().string())
        }
    }

    String getAccessTokenLib() {
        OAuth20Service service = new ServiceBuilder("lpt")
                .httpClient(new OkHttpHttpClient(client))
                .build(KeycloakApiPatched.instance("http://habitcentric.demo", "habitcentric"))

        def token = service.getAccessTokenPasswordGrant("lpt", "lpt")
        token.accessToken
    }

    String getAccessToken() {
        RequestBody body = new FormBody.Builder()
                .add("username", "lpt")
                .add("password", "lpt")
                .add("grant_type", "password")
                .add("client_id", "lpt")
                .build()

        Request tokenRequest = new Request.Builder()
                .url("http://habitcentric.demo/auth/realms/habitcentric/protocol/openid-connect/token")
                .post(body)
                .build();

        try (Response response = client.newCall(tokenRequest).execute()) {
            def parse = new JsonSlurper().parse(response.body().charStream());
            parse.access_token
        }
    }

    @Immutable
    class ResponseDto {
        int code
        String body
    }
}
