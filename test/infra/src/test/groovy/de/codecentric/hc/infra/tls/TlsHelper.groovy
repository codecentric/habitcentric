package de.codecentric.hc.infra.tls

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.SecureRandom
import java.security.cert.X509Certificate

class TlsHelper {
    static SSLSocketFactory createSSLSocketFactory() {
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
        sc.getSocketFactory();
    }

    static X509TrustManager trustAllCerts() {
        new TrustAllCerts()
    }

    static HostnameVerifier trustAllHosts() {
        new TrustAllHostnameVerifier()
    }

    private static class TrustAllCerts implements X509TrustManager {
        void checkClientTrusted(X509Certificate[] chain, String authType) {}

        void checkServerTrusted(X509Certificate[] chain, String authType) {}

        X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        boolean verify(String hostname, SSLSession session) { return true }
    }
}
