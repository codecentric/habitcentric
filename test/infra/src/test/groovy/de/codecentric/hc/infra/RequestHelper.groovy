package de.codecentric.hc.infra

import groovy.json.JsonSlurper
import groovy.transform.Immutable
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

class RequestHelper {
    private static final String OIDC_TOKEN_URL = "http://habitcentric.demo/auth/realms/habitcentric/protocol/openid-connect/token"
    private static final String OIDC_USERNAME = "testing"
    private static final String OIDC_PASSWORD = "testing"
    private static final String OIDC_CLIENT_ID = "testing"
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
            mapToResponse(response)
        }
    }

    private ResponseDto mapToResponse(Response response) {
        new ResponseDto(response.code(), response.body().string(), response.headers().toMultimap())
    }

    ResponseDto getWithAuth(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer ${getAccessToken()}")
                .build();
        try (def response = client.newCall(request).execute()) {
            mapToResponse(response)
        }
    }

    String getAccessToken() {
        RequestBody body = new FormBody.Builder()
                .add("username", OIDC_USERNAME)
                .add("password", OIDC_PASSWORD)
                .add("client_id", OIDC_CLIENT_ID)
                .add("grant_type", "password")
                .build()

        Request tokenRequest = new Request.Builder()
                .url(OIDC_TOKEN_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(tokenRequest).execute()) {
            def tokenResponse = new JsonSlurper().parse(response.body().charStream());
            tokenResponse.access_token
        }
    }

    @Immutable
    class ResponseDto {
        int code
        String body
        Map<String, List<String>> headers
    }
}
