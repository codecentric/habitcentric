package de.codecentric.hc


import de.codecentric.hc.infra.Environment
import de.codecentric.hc.infra.HttpHelper
import spock.lang.Narrative
import spock.lang.Specification

@Narrative('''Test some basic properties a habitcentric deployment should have''')
class HabitcentricSpec extends Specification {
    private HttpHelper httpHelper = new HttpHelper()

    def "should redirect to /ui from the root"() {
        given:
        def url = Environment.baseUrl

        when:
        def response = httpHelper.create().get(url).execute()

        then:
        response.code == 301 || response.code == 302
        response.headers.location == [Environment.baseUrlWithPath("/ui")]
    }
}
