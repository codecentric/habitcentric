package de.codecentric.habitcentric.track.habit.jwt;

import static de.codecentric.habitcentric.track.habit.matcher.HabitApiMatcher.hasHabitIdViolationError;
import static de.codecentric.habitcentric.track.habit.matcher.HabitApiMatcher.hasUserIdViolationError;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.codecentric.habitcentric.track.RestAssuredTest;
import io.restassured.specification.RequestSpecification;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class HabitTrackingControllerJwtRestAssuredTest extends RestAssuredTest {

  private final String urlTemplate = "/track/habits/{habitId}";

  private final Long habitId = 123L;

  @Autowired private JdbcTemplate jdbcTemplate;

  @AfterEach
  public void cleanUp() {
    String[] tableNames = {"HC_TRACK.HABIT_TRACKING"};
    JdbcTestUtils.deleteFromTables(jdbcTemplate, tableNames);
  }

  @Test
  public void shouldReturnEmptyArrayWithoutTrackRecords() {
    givenValidJwt().when().get(urlTemplate, habitId).then().statusCode(200).body(equalTo("[]"));
  }

  @Test
  public void shouldReturnCreatedTrackRecords() {

    String[] expected = {"2018-12-31", "2019-01-01", "2019-01-31"};

    assertThat(
            givenValidJwt()
                .contentType(JSON)
                .when()
                .body(expected)
                .put(urlTemplate, habitId)
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body()
                .as(String[].class))
        .containsExactly(expected);

    assertThat(
            givenValidJwt()
                .when()
                .get(urlTemplate, habitId)
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body()
                .as(String[].class))
        .containsExactly(expected);
  }

  @Test
  public void shouldReturnUpdatedTrackRecords() {

    String[] inserted = {"2018-12-31", "2019-01-01", "2019-01-31"};
    String[] updated = {"2019-02-28", "2019-03-21"};

    assertThat(
            givenValidJwt()
                .contentType(JSON)
                .when()
                .body(inserted)
                .put(urlTemplate, habitId)
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body()
                .as(String[].class))
        .containsExactly(inserted);

    assertThat(
            givenValidJwt()
                .contentType(JSON)
                .when()
                .body(updated)
                .put(urlTemplate, habitId)
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body()
                .as(String[].class))
        .containsExactly(updated);

    assertThat(
            givenValidJwt()
                .when()
                .get(urlTemplate, habitId)
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body()
                .as(String[].class))
        .containsExactly(updated);
  }

  @Test
  public void shouldIgnoreDuplicatesAndOrderTrackRecordsChronological() {

    String[] inserted = {"2019-01-01", "2018-12-31", "2019-01-01"};
    String[] expectedAfterInsert = {"2018-12-31", "2019-01-01"};
    String[] updated = {"2019-03-21", "2019-03-21", "2018-01-01"};
    String[] expectedAfterUpdated = {"2018-01-01", "2019-03-21"};

    assertThat(
            givenValidJwt()
                .contentType(JSON)
                .when()
                .body(inserted)
                .put(urlTemplate, habitId)
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body()
                .as(String[].class))
        .containsExactly(expectedAfterInsert);

    assertThat(
            givenValidJwt()
                .contentType(JSON)
                .when()
                .body(updated)
                .put(urlTemplate, habitId)
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body()
                .as(String[].class))
        .containsExactly(expectedAfterUpdated);

    assertThat(
            givenValidJwt()
                .when()
                .get(urlTemplate, habitId)
                .then()
                .statusCode(200)
                .contentType(JSON)
                .extract()
                .body()
                .as(String[].class))
        .containsExactly(expectedAfterUpdated);
  }

  @Test
  public void shouldRejectGetRequestsWithInvalidUserId() {
    givenInvalidUserIdJwt()
        .when()
        .get(urlTemplate, habitId)
        .then()
        .statusCode(400)
        .contentType(JSON)
        .body(hasUserIdViolationError());
  }

  @Test
  public void shouldRejectGetRequestsWithUserIdLongerThan64Characters() {
    givenUserIdLongerThan64CharactersJwt()
        .when()
        .get(urlTemplate, habitId)
        .then()
        .statusCode(400)
        .contentType(JSON)
        .body(hasUserIdViolationError());
  }

  @Test
  public void shouldRejectGetRequestsWithoutPositiveHabitId() {
    givenValidJwt()
        .when()
        .get(urlTemplate, 0)
        .then()
        .statusCode(400)
        .contentType(JSON)
        .body(hasHabitIdViolationError());
  }

  @Test
  public void shouldRejectPutRequestsWithInvalidUserId() {
    givenInvalidUserIdJwt()
        .contentType(JSON)
        .body(new LocalDate[0])
        .when()
        .put(urlTemplate, habitId)
        .then()
        .statusCode(400)
        .contentType(JSON)
        .body(hasUserIdViolationError());
  }

  @Test
  public void shouldRejectPutRequestsWithUserIdLongerThan64Characters() {
    givenUserIdLongerThan64CharactersJwt()
        .contentType(JSON)
        .body(new LocalDate[0])
        .when()
        .put(urlTemplate, habitId)
        .then()
        .statusCode(400)
        .contentType(JSON)
        .body(hasUserIdViolationError());
  }

  @Test
  public void shouldRejectPutRequestsWithoutPositiveHabitId() {
    givenValidJwt()
        .contentType(JSON)
        .body(new LocalDate[0])
        .when()
        .put(urlTemplate, 0)
        .then()
        .statusCode(400)
        .contentType(JSON)
        .body(hasHabitIdViolationError());
  }

  @Test
  public void shouldRejectPutRequestsWithInvalidContentType() {
    givenValidJwt()
        .body(new LocalDate[0])
        .when()
        .put(urlTemplate, 0)
        .then()
        .statusCode(415)
        .contentType(JSON)
        .body("error", equalTo("Unsupported Media Type"))
        .body("message", equalTo("Content-Type 'text/plain;charset=ISO-8859-1' is not supported."));
  }

  @Test
  public void shouldRejectPutRequestsWithoutBody() {
    givenValidJwt()
        .contentType(JSON)
        .when()
        .put(urlTemplate, 0)
        .then()
        .statusCode(400)
        .contentType(JSON)
        .body("error", equalTo("Bad Request"))
        .body("message", startsWith("Required request body is missing"));
  }

  @Test
  public void shouldRejectPutRequestsWithoutInvalidDates() {
    String body = "[\"2019-02-31\"]";
    givenValidJwt()
        .contentType(JSON)
        .body(body)
        .when()
        .put(urlTemplate, 0)
        .then()
        .statusCode(400)
        .contentType(JSON)
        .body("error", equalTo("Bad Request"))
        .body(
            "message",
            startsWith(
                "JSON parse error: Cannot deserialize value of type `java.time.LocalDate` from String \"2019-02-31\""))
        .body(
            "message",
            containsString("Text '2019-02-31' could not be parsed: Invalid date 'FEBRUARY 31'"));
  }

  private RequestSpecification givenValidJwt() {
    final String authorizationHeader =
        "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmMuZGVmIn0"
            + ".WwSUwDaAy06itFh4asthobE0SNJihIsxaw6koNNsjZI";
    return givenAuthorizationHeader(authorizationHeader);
  }

  private RequestSpecification givenInvalidUserIdJwt() {
    String authorizationHeaderInvalidUserId =
        "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIgICAifQ"
            + ".vTUtOoNj968RAGBiApe3zZa4RsHJFszq5a4lj6xEuOk";
    return givenAuthorizationHeader(authorizationHeaderInvalidUserId);
  }

  private RequestSpecification givenUserIdLongerThan64CharactersJwt() {
    String authorizationHeader65CharUserId =
        "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9"
            + ".eyJzdWIiOiJhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYWFhYSJ9"
            + ".sIFKsUtJhDnlVDyD2VOJp7nx0lGZ7zLRiix8yt3MnFU";
    return givenAuthorizationHeader(authorizationHeader65CharUserId);
  }

  private RequestSpecification givenAuthorizationHeader(String authorizationHeader) {
    return given().header(HttpHeaders.AUTHORIZATION, authorizationHeader);
  }
}
