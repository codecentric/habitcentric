package de.codecentric.hc

import de.codecentric.hc.infra.DigestHelper
import de.codecentric.hc.infra.Environment
import de.codecentric.hc.infra.HttpHelper
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Stepwise

import java.time.LocalDate
import java.time.LocalDateTime

@Narrative('''
Test Report Service functionality
''')
@Stepwise
class ReportServiceSpec extends Specification {
    private HttpHelper httpHelper = new HttpHelper()
    private static final String runId = DigestHelper.generateUniqueString(LocalDateTime.now().toString())

    def "should be able to create a achievement report"() {
        given:
        def habit = [
                name    : "Play guitar ${runId}",
                schedule: [
                        repetitions: 5,
                        frequency  : 'DAILY'
                ]
        ]
        def habitResponse = httpHelper.create().auth().post("${Environment.baseUrl}/habits", habit).execute()
        assert habitResponse.code == 201
        assert habitResponse.headers.location
        def habitId = Integer.parseInt(habitResponse.headers.location.get(0).split("/").last())
        assert habitId > 0

        def tracking = [
                LocalDate.now(),
                LocalDate.now().minusDays(1),
                LocalDate.now().minusDays(2),
                LocalDate.now().minusDays(3),
                LocalDate.now().minusDays(4),
                LocalDate.now().minusDays(7),
                LocalDate.now().minusDays(8),
                LocalDate.now().minusDays(9),
                LocalDate.now().minusDays(20),
                LocalDate.now().minusDays(21),
                LocalDate.now().minusDays(22),
                LocalDate.now().minusDays(23),
        ].collect { it.toString() }
        def trackResponse = httpHelper.create().auth().put("${Environment.baseUrl}/track/habits/${habitId}", tracking).execute();
        assert trackResponse.code == 200

        when:
        def response = httpHelper.create().auth().get("${Environment.baseUrl}/report/achievement").execute()

        then:
        response.code == 200
        response.body.week > 0
        response.body.month > 0
    }
}
