package de.codecentric.hc.mesh

import de.codecentric.hc.infra.Environment
import de.codecentric.hc.infra.K8sHelper
import de.codecentric.hc.infra.RequestHelper
import spock.lang.Requires
import spock.lang.Specification

@Requires({ Environment.Istio })
class IstioSecuritySpec extends Specification {
    private RequestHelper oauthHelper = new RequestHelper()
    private static helperPodNamespace = "hc-ui"
    private static helperPodName = "habitcentric-pen-test"
    private static helper = new K8sHelper()

    void setupSpec() {
        helper.createAlpineCmdRunnerWithPackages(
                helperPodName,
                helperPodNamespace,
                ["istio-injection": "enabled"],
                ["curl", "postgresql"],
                ["curl", "psql"]
        )
    }

    void cleanupSpec() {
        helper.deletePod(helperPodName, helperPodNamespace)
    }

    def "should forbid request to #name service without oidc token"() {
        when:
        def url = Environment.baseUrlWithPath(path)
        def response = oauthHelper.get(url)

        then:
        response.code == 403

        where:
        name     | path
        'Habit'  | '/habits'
        'Track'  | '/track'
        'Report' | '/report'
    }

    def "should allow request to #name service with oidc token"() {
        when:
        def url = Environment.baseUrlWithPath(path)
        def response = oauthHelper.getWithAuth(url)

        then:
        response.code == status

        where:
        name     | path                  || status
        'Habit'  | '/habits'             || 200
        'Track'  | '/track/habits'       || 404
        'Report' | '/report/achievement' || 200
    }

    def "should forbid access to service: #service"() {
        given:
        helper.waitForReadyPod(helperPodName, helperPodNamespace)

        when:
        def result = helper.executeCommandInPod(helperPodName, helperPodNamespace, command)

        then:
        result.stdout == expectedOutput || result.stderr == expectedOutput

        where:
        service             | command                                                                   || expectedOutput
        "ui"                | "curl -sS ui.hc-ui/ui"                                                    || "upstream connect error or disconnect/reset before headers. reset reason: connection failure"
        "habit"             | "curl -sS habit.hc-habit:8080"                                            || "upstream connect error or disconnect/reset before headers. reset reason: connection failure"
        "habit-postgres"    | "psql -h habit-habit-postgresql.hc-habit -p 5432 -c \"SELECT 'nope'\" -w" || "psql: error: server closed the connection unexpectedly\n\tThis probably means the server terminated abnormally\n\tbefore or while processing the request.\n"
        "track"             | "curl -sS track.hc-track:8080"                                            || "upstream connect error or disconnect/reset before headers. reset reason: connection failure"
        "track-postgres"    | "psql -h track-track-postgresql.hc-track -p 5432 -c \"SELECT 'nope'\" -w" || "psql: error: server closed the connection unexpectedly\n\tThis probably means the server terminated abnormally\n\tbefore or while processing the request.\n"
        "report"            | "curl -sS report.hc-report:8080"                                          || "upstream connect error or disconnect/reset before headers. reset reason: connection failure"
        "keycloak"          | "curl -sS keycloak-http.hc-keycloak"                                      || "upstream connect error or disconnect/reset before headers. reset reason: connection failure"
        "keycloak-postgres" | "psql -h keycloak-postgresql.hc-keycloak -p 5432 -c \"SELECT 'nope'\" -w" || "psql: error: server closed the connection unexpectedly\n\tThis probably means the server terminated abnormally\n\tbefore or while processing the request.\n"
    }
}
