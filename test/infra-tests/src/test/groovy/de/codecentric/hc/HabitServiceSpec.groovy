package de.codecentric.hc

import de.codecentric.hc.infra.DigestHelper
import de.codecentric.hc.infra.Environment
import de.codecentric.hc.infra.HttpHelper
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Stepwise

import java.time.LocalDateTime

@Narrative('''
Test Habit Service functionality
''')
@Stepwise
class HabitServiceSpec extends Specification {
    private HttpHelper httpHelper = new HttpHelper()
    private static final String runId = DigestHelper.generateUniqueString(LocalDateTime.now().toString())

    def "should be able to create a habit"() {
        given:
        def url = "${Environment.baseUrl}/habits"
        def habit = [
                name    : "Play guitar ${runId}",
                schedule: [
                        repetitions: 5,
                        frequency  : 'MONTHLY'
                ]
        ]

        when:
        def response = httpHelper.create().auth().post(url, habit).execute();

        then:
        response.code == 201
        response.headers.location
    }

    def "should be able to load habits"() {
        given:
        def url = "${Environment.baseUrl}/habits"

        when:
        def response = httpHelper.create().auth().get(url).execute()

        then:
        response.code == 200
        response.body.size() > 0
    }

    def "should be able to load a habit by id"() {
        given:
        def habitsResponse = httpHelper.create().auth().get("${Environment.baseUrl}/habits").execute()
        assert habitsResponse.code == 200
        def url = "${Environment.baseUrl}/habits/${habitsResponse.getBody().last().id}"

        when:
        def response = httpHelper.create().auth().get(url).execute()

        then:
        response.code == 200
        response.body.size() > 0
        response.body.name ==~ /Play guitar.*/
        response.body.schedule.frequency == "MONTHLY"
        response.body.schedule.repetitions == 5
    }

    def "should delete habit"() {
        given:
        def habitsResponse = httpHelper.create().auth().get("${Environment.baseUrl}/habits").execute()
        assert habitsResponse.code == 200
        def url = "${Environment.baseUrl}/habits/${habitsResponse.getBody().last().id}"

        when:
        def response = httpHelper.create().auth().delete(url).execute()

        then:
        response.code == 200
    }
}
