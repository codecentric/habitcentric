package de.codecentric.hc.infra

class Environment {
    private static String ENVIRONMENT_PROPERTY = "habitcentric.environment"
    private static String HTTPS_PROPERTY = "habitcentric.https"

    public static boolean Istio = isEnvironment("istio")
    public static boolean Linkerd = isEnvironment("linkerd")
    public static boolean https = isHttps()
    public static String baseUrl = isHttps() ? "https://habitcentric.demo" : "http://habitcentric.demo"

    static isEnvironment(String name) {
        getSystemPropertyWithProperDefault(ENVIRONMENT_PROPERTY, "istio") == name
    }

    static isHttps() {
        getSystemPropertyWithProperDefault(HTTPS_PROPERTY, "false").toBoolean()
    }

    static String baseUrlWithPath(path) {
        return "$baseUrl$path"
    }

    /**
     * Tries to get a system property or returns the default.
     *
     * <p>
     * Why not {@link System#getProperty(java.lang.String, java.lang.String)}?<br/>
     * Because the property will be set when run via gradle. This causes the default to be ignored.
     * To work around this we need to use the elvis operator,
     * in groovy an empty string evaluates to false.
     * </p>
     */
    private static getSystemPropertyWithProperDefault(String key, String defaultVal) {
        System.getProperty(key) ?: defaultVal
    }
}
