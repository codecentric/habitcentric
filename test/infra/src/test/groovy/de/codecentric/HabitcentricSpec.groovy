package de.codecentric

import de.codecentric.infra.HabitcentricOauthHelper
import spock.lang.Specification

class HabitcentricSpec extends Specification {
    private HabitcentricOauthHelper oauthHelper = new HabitcentricOauthHelper()

    def "should redirect to /ui from the root"() {
        given:
        def url = "http://habitcentric.demo"

        when:
        def response = oauthHelper.get(url)

        then:
        response.code == 301 || response.code == 302
    }
}
