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
                ["curl", "postgresql"]
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
        result.stdout ==~ expectedOutput || result.stderr ==~ expectedOutput

        where:
        service             | command                                                                    || expectedOutput
        "ui"                | "curl ui.hc-ui:9004/ui"                                                    || "RBAC: access denied"
        "habit"             | "curl habit.hc-habit:9001"                                                 || "RBAC: access denied"
        "habit-postgres"    | "psql -h habit-habit-postgresql.hc-habit -p 10001 -c \"SELECT 'nope'\" -w" || ".*\n\tThis probably means the server terminated abnormally\n\tbefore or while processing the request.\n"
        "track"             | "curl track.hc-track:9002"                                                 || "RBAC: access denied"
        "track-postgres"    | "psql -h track-track-postgresql.hc-track -p 10002 -c \"SELECT 'nope'\" -w" || ".*\n\tThis probably means the server terminated abnormally\n\tbefore or while processing the request.\n"
        "report"            | "curl report.hc-report:9003"                                               || "RBAC: access denied"
        "keycloak"          | "curl keycloak-http.hc-keycloak:8080"                                      || "RBAC: access denied"
        "keycloak-postgres" | "psql -h keycloak-postgresql.hc-keycloak -p 10003 -c \"SELECT 'nope'\" -w" || ".*\n\tThis probably means the server terminated abnormally\n\tbefore or while processing the request.\n"
    }
}
