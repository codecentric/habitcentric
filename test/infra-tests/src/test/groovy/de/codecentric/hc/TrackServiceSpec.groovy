package de.codecentric.hc


import de.codecentric.hc.infra.Environment
import de.codecentric.hc.infra.HttpHelper
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Stepwise

@Narrative('''
Test Track Service functionality
''')
@Stepwise
class TrackServiceSpec extends Specification {
    private HttpHelper httpHelper = new HttpHelper()

    def "should be able to track a habit"() {
        given:
        def url = "${Environment.baseUrl}/track/habits/10"
        def tracking = [
                "2522-09-01",
                "2522-09-02",
        ]

        when:
        def response = httpHelper.create().auth().put(url, tracking).execute();

        then:
        response.code == 200
        response.body == tracking
    }

    def "should be able to load tracking for a habit"() {
        given:
        def url = "${Environment.baseUrl}/track/habits/10"

        when:
        def response = httpHelper.create().auth().get(url).execute();

        then:
        response.code == 200
        response.body.size() == 2
    }

    def "should be able to adjust tracks"() {
        given:
        def url = "${Environment.baseUrl}/track/habits/10"
        def tracking = [
                "2522-09-01",
        ]
        def updateResponse = httpHelper.create().auth().put(url, tracking).execute()
        assert updateResponse.code == 200

        when:
        def response = httpHelper.create().auth().get(url).execute();

        then:
        response.code == 200
        response.body.size() == 1
    }
}
