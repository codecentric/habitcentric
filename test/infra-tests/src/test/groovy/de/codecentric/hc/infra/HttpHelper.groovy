package de.codecentric.hc.infra

import de.codecentric.hc.infra.tls.TlsHelper
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import groovy.transform.Immutable
import groovy.transform.ImmutableOptions
import okhttp3.*

class HttpHelper {
    private static final String OIDC_TOKEN_URL = Environment.baseUrlWithPath("/auth/realms/habitcentric/protocol/openid-connect/token")
    private static final String OIDC_USERNAME = "testing"
    private static final String OIDC_PASSWORD = "testing"
    private static final String OIDC_CLIENT_ID = "testing"
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8")
    private OkHttpClient client

    HttpHelper() {
        client = new OkHttpClient().newBuilder()
                .followRedirects(false)
                .followSslRedirects(false)
                .sslSocketFactory(TlsHelper.createSSLSocketFactory(), TlsHelper.trustAllCerts())
                .hostnameVerifier(TlsHelper.trustAllHosts())
                .build()
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
                .build()

        try (Response response = client.newCall(tokenRequest).execute()) {
            def tokenResponse = new JsonSlurper().parse(response.body().charStream())
            tokenResponse.access_token
        }
    }

    Builder create() {
        return new Builder(client)
    }

    private class Builder {
        private Request.Builder request
        private OkHttpClient client

        Builder(OkHttpClient client) {
            request = new Request.Builder()
            this.client = client
        }

        Builder auth() {
            request = request.header("Authorization", "Bearer ${getAccessToken()}")
            return this
        }

        Builder post(String url, Object body) {
            request = request.post(RequestBody.create(JsonOutput.toJson(body), JSON))
                    .url(url)
            return this
        }

        Builder put(String url, Object body) {
            request = request.put(RequestBody.create(JsonOutput.toJson(body), JSON))
                    .url(url)
            return this
        }

        Builder get(String url) {
            request = request.url(url)
            return this
        }

        Builder delete(String url) {
            request = request.url(url)
            return this
        }

        ResponseDto execute() {
            try (def response = client.newCall(request.build()).execute()) {
                mapToResponse(response)
            }
        }

        private ResponseDto mapToResponse(Response response) {
            if (response.body().contentLength() == 0) {
                return new ResponseDto(response.code(), null, response.headers().toMultimap())
            }

            def isJson = response.header("Content-Type").contains("application/json")

            Object body = isJson
                    ? new JsonSlurper().parse(response.body().charStream())
                    : response.body().toString()

            new ResponseDto(response.code(), body, response.headers().toMultimap())

        }
    }

    @Immutable
    @ImmutableOptions(knownImmutables = ["body"])
    class ResponseDto {
        int code
        Object body
        Map<String, List<String>> headers
    }
}
