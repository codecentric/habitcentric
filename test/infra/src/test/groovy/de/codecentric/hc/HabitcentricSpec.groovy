package de.codecentric.hc

import de.codecentric.hc.infra.RequestHelper
import spock.lang.Narrative
import spock.lang.Specification

@Narrative('''Test some basic properties a habitcentric deployment should have''')
class HabitcentricSpec extends Specification {
    private RequestHelper oauthHelper = new RequestHelper()

    def "should redirect to /ui from the root"() {
        given:
        def url = "http://habitcentric.demo"

        when:
        def response = oauthHelper.get(url)

        then:
        response.code == 301 || response.code == 302
        response.headers.location == ["http://habitcentric.demo/ui"]
    }
}
