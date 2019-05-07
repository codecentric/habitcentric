package de.codecentric.hc.track.habits;

import de.codecentric.hc.track.RestAssuredTest;
import de.codecentric.hc.track.errors.ApiErrorExpectations;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;

import static de.codecentric.hc.track.errors.ApiErrorMatcher.hasApiErrors;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;

public class HabitTrackingControllerRestAssuredTest extends RestAssuredTest {

    private final String urlTemplate = "/track/users/{userId}/habits/{habitId}";
    private final String userId = "abc.def";
    private final Long habitId = 123L;

    @ClassRule
    public static JdbcDatabaseContainer database = new PostgreSQLContainer();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @After
    public void cleanUp() {
        String[] tableNames = {"HC_TRACK.HABIT_TRACKING"};
        JdbcTestUtils.deleteFromTables(jdbcTemplate, tableNames);
    }

    @Test
    public void shouldReturnEmptyArrayWithoutTrackRecords() {
        given()
                .when().get(urlTemplate, userId, habitId)
                .then().statusCode(200).body(equalTo("[]"));
    }

    @Test
    public void shouldReturnCreatedTrackRecords() {

        String[] expected = {"2018-12-31", "2019-01-01", "2019-01-31"};

        assertThat(given().contentType(JSON)
                .when().body(expected).put(urlTemplate, userId, habitId)
                .then().statusCode(200).contentType(JSON)
                .extract().body().as(String[].class)
        ).containsExactly(expected);

        assertThat(given()
                .when().get(urlTemplate, userId, habitId)
                .then().statusCode(200).contentType(JSON)
                .extract().body().as(String[].class)
        ).containsExactly(expected);
    }

    @Test
    public void shouldReturnUpdatedTrackRecords() {

        String[] inserted = {"2018-12-31", "2019-01-01", "2019-01-31"};
        String[] updated = {"2019-02-28", "2019-03-21"};

        assertThat(given().contentType(JSON)
                .when().body(inserted).put(urlTemplate, userId, habitId)
                .then().statusCode(200).contentType(JSON)
                .extract().body().as(String[].class)
        ).containsExactly(inserted);

        assertThat(given().contentType(JSON)
                .when().body(updated).put(urlTemplate, userId, habitId)
                .then().statusCode(200).contentType(JSON)
                .extract().body().as(String[].class)
        ).containsExactly(updated);

        assertThat(given()
                .when().get(urlTemplate, userId, habitId)
                .then().statusCode(200).contentType(JSON)
                .extract().body().as(String[].class)
        ).containsExactly(updated);
    }

    @Test
    public void shouldIgnoreDuplicatesAndOrderTrackRecordsChronological() {

        String[] inserted = {"2019-01-01", "2018-12-31", "2019-01-01"};
        String[] expectedAfterInsert = {"2018-12-31", "2019-01-01"};
        String[] updated = {"2019-03-21", "2019-03-21", "2018-01-01"};
        String[] expectedAfterUpdated = {"2018-01-01", "2019-03-21"};

        assertThat(given().contentType(JSON)
                .when().body(inserted).put(urlTemplate, userId, habitId)
                .then().statusCode(200).contentType(JSON)
                .extract().body().as(String[].class)
        ).containsExactly(expectedAfterInsert);

        assertThat(given().contentType(JSON)
                .when().body(updated).put(urlTemplate, userId, habitId)
                .then().statusCode(200).contentType(JSON)
                .extract().body().as(String[].class)
        ).containsExactly(expectedAfterUpdated);

        assertThat(given()
                .when().get(urlTemplate, userId, habitId)
                .then().statusCode(200).contentType(JSON)
                .extract().body().as(String[].class)
        ).containsExactly(expectedAfterUpdated);
    }

    @Test
    public void shouldRejectGetRequestsWithInvalidUserId() {
        given()
                .when().get(urlTemplate, "     ", habitId)
                .then().statusCode(400).contentType(JSON)
                .body(hasApiErrors(ApiErrorExpectations.EXPECTED_USER_ID_VIOLATION));
    }

    @Test
    public void shouldRejectGetRequestsWithUserIdLongerThan64Characters() {
        given()
                .when().get(urlTemplate, StringUtils.repeat("a", 65), habitId)
                .then().statusCode(400).contentType(JSON)
                .body(hasApiErrors(ApiErrorExpectations.EXPECTED_USER_ID_VIOLATION));
    }

    @Test
    public void shouldRejectGetRequestsWithoutPositiveHabitId() {
        given()
                .when().get(urlTemplate, userId, 0)
                .then().statusCode(400).contentType(JSON)
                .body(hasApiErrors(ApiErrorExpectations.EXPECTED_HABIT_ID_VIOLATION));
    }

    @Test
    public void shouldRejectPutRequestsWithInvalidUserId() {
        given().contentType(JSON).body(new LocalDate[0])
               .when().put(urlTemplate, "     ", habitId)
               .then().statusCode(400).contentType(JSON)
               .body(hasApiErrors(ApiErrorExpectations.EXPECTED_USER_ID_VIOLATION));
    }

    @Test
    public void shouldRejectPutRequestsWithUserIdLongerThan64Characters() {
        given().contentType(JSON).body(new LocalDate[0])
               .when().put(urlTemplate, StringUtils.repeat("a", 65), habitId)
               .then().statusCode(400).contentType(JSON)
               .body(hasApiErrors(ApiErrorExpectations.EXPECTED_USER_ID_VIOLATION));
    }

    @Test
    public void shouldRejectPutRequestsWithoutPositiveHabitId() {
        given().contentType(JSON).body(new LocalDate[0])
               .when().put(urlTemplate, userId, 0)
               .then().statusCode(400).contentType(JSON)
               .body(hasApiErrors(ApiErrorExpectations.EXPECTED_HABIT_ID_VIOLATION));
    }

    @Test
    public void shouldRejectPutRequestsWithInvalidContentType() {
        given().body(new LocalDate[0])
                .when().put(urlTemplate, userId, 0)
                .then().statusCode(415).contentType(JSON)
                .body("error", equalTo("Unsupported Media Type"))
                .body("message", equalTo("Content type 'text/plain;charset=ISO-8859-1' not supported"));
    }

    @Test
    public void shouldRejectPutRequestsWithoutBody() {
        given().contentType(JSON)
                .when().put(urlTemplate, userId, 0)
                .then().statusCode(400).contentType(JSON)
                .body("error", equalTo("Bad Request"))
                .body("message", startsWith("Required request body is missing"));
    }

    @Test
    public void shouldRejectPutRequestsWithoutInvalidDates() {
        String body = "[\"2019-02-31\"]";
        given().contentType(JSON).body(body)
                .when().put(urlTemplate, userId, 0)
                .then().statusCode(400).contentType(JSON)
                .body("error", equalTo("Bad Request"))
                .body("message", startsWith("JSON parse error: Cannot deserialize value of type `java.time.LocalDate` from String \"2019-02-31\""))
                .body("message", containsString("Text '2019-02-31' could not be parsed: Invalid date 'FEBRUARY 31'"));
    }
}
