package de.codecentric.hc.habit.habits;

import static de.codecentric.hc.habit.habits.Habit.Schedule.Frequency.DAILY;
import static de.codecentric.hc.habit.habits.Habit.Schedule.Frequency.WEEKLY;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;

import de.codecentric.hc.habit.habits.Habit.ModificationRequest;
import de.codecentric.hc.habit.habits.Habit.Schedule;
import de.codecentric.hc.habit.testing.RestAssuredTest;
import io.restassured.http.Header;
import java.sql.SQLException;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

// TODO: Add integration tests covering invalid or missing Authorization header
@Testcontainers
public class HabitControllerJwtIntTest extends RestAssuredTest {

  private static final String TABLE_NAME = "hc_habit.HABIT";
  private static final Header DEFAULT_AUTHORIZATION_HEADER =
      new Header(
          HttpHeaders.AUTHORIZATION,
          "Bearer "
              + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkZWZhdWx0In0.2E88ZlFE4Tor8d5gRU2451WrLtDavGfgbFf8ZuKBRxM");
  private static final Schedule DEFAULT_SCHEDULE = new Schedule(1, DAILY);

  @Container public static final JdbcDatabaseContainer DATABASE = new PostgreSQLContainer();

  @Autowired private JdbcTemplate jdbcTemplate;

  @AfterEach
  public void cleanUp() throws SQLException {
    JdbcTestUtils.deleteFromTables(jdbcTemplate, TABLE_NAME);
  }

  @Test
  public void getHabits() throws InterruptedException {

    String[] inserted = {"Jogging", "Play guitar", "Meditate"};
    String[] expected = {"Jogging", "Meditate", "Play guitar"};

    Stream.of(inserted).forEach(name -> insertHabit(name));

    given()
        .header(DEFAULT_AUTHORIZATION_HEADER)
        .when()
        .get("/habits")
        .then()
        .statusCode(OK.value())
        .body("name", contains(expected))
        .body("id", everyItem(greaterThan(0)));
  }

  @Test
  public void getHabitsOrderedByNameAscending() throws InterruptedException {

    String[] inserted = {"c", "ä", "d", "b", "a", "A", "ac", "s", "af", "x", "ß"};
    String[] expected = {"a", "A", "ä", "ac", "af", "b", "c", "d", "s", "ß", "x"};

    Stream.of(inserted).forEach(name -> insertHabit(name));

    given()
        .header(DEFAULT_AUTHORIZATION_HEADER)
        .when()
        .get("/habits")
        .then()
        .statusCode(OK.value())
        .body("name", contains(expected))
        .body("id", everyItem(greaterThan(0)));
  }

  @Test
  public void createHabit() {

    ModificationRequest body =
        ModificationRequest.builder().name("New habit").schedule(new Schedule(2, WEEKLY)).build();

    assertThat(numberOfHabits()).isZero();

    String location =
        given()
            .header(DEFAULT_AUTHORIZATION_HEADER)
            .contentType(JSON)
            .body(body)
            .when()
            .post("/habits")
            .then()
            .statusCode(CREATED.value())
            .body(isEmptyOrNullString())
            .extract()
            .header("location");

    assertThat(numberOfHabits()).isOne();

    Habit habit =
        given()
            .header(DEFAULT_AUTHORIZATION_HEADER)
            .when()
            .get(location)
            .then()
            .statusCode(OK.value())
            .extract()
            .body()
            .as(Habit.class);
    assertThat(habit.getName()).isEqualTo(body.getName());
    assertThat(habit.getSchedule()).isEqualTo(body.getSchedule());
  }

  @Test
  public void createHabitWithBlankName() {
    ModificationRequest body =
        ModificationRequest.builder().name("   ").schedule(DEFAULT_SCHEDULE).build();

    given()
        .header(DEFAULT_AUTHORIZATION_HEADER)
        .contentType(JSON)
        .body(body)
        .when()
        .post("/habits")
        .then()
        .statusCode(BAD_REQUEST.value())
        .body("errors[0].objectName", equalTo("modificationRequest"))
        .body("errors[0].field", equalTo("name"))
        .body("errors[0].code", equalTo("NotBlank"));

    assertThat(numberOfHabits()).isZero();
  }

  @Test
  public void createHabitWithDuplicateName() {
    String habitName = "ABC";

    insertHabit(habitName);

    ModificationRequest body =
        ModificationRequest.builder().name(habitName).schedule(DEFAULT_SCHEDULE).build();

    given()
        .header(DEFAULT_AUTHORIZATION_HEADER)
        .contentType(JSON)
        .body(body)
        .when()
        .post("/habits")
        .then()
        .statusCode(BAD_REQUEST.value())
        .body("message", equalTo("Please choose a unique habit name."));

    assertThat(numberOfHabits()).isOne();
  }

  @Test
  public void createHabitWith64CharacterName() {
    String habitName = StringUtils.repeat("a", 64);

    ModificationRequest body =
        ModificationRequest.builder().name(habitName).schedule(DEFAULT_SCHEDULE).build();

    given()
        .header(DEFAULT_AUTHORIZATION_HEADER)
        .contentType(JSON)
        .body(body)
        .when()
        .post("/habits")
        .then()
        .statusCode(CREATED.value())
        .body(isEmptyOrNullString());

    assertThat(numberOfHabits()).isOne();
  }

  @Test
  public void createHabitWith65CharacterName() {
    String habitName = StringUtils.repeat("a", 65);

    ModificationRequest body =
        ModificationRequest.builder().name(habitName).schedule(DEFAULT_SCHEDULE).build();

    given()
        .header(DEFAULT_AUTHORIZATION_HEADER)
        .contentType(JSON)
        .body(body)
        .when()
        .post("/habits")
        .then()
        .statusCode(BAD_REQUEST.value())
        .body("errors[0].objectName", equalTo("modificationRequest"))
        .body("errors[0].field", equalTo("name"))
        .body("errors[0].code", equalTo("Size"));

    assertThat(numberOfHabits()).isZero();
  }

  @Test
  public void createHabitWithoutSchedule() {
    ModificationRequest body = ModificationRequest.builder().name("Jogging").build();

    given()
        .header(DEFAULT_AUTHORIZATION_HEADER)
        .contentType(JSON)
        .body(body)
        .when()
        .post("/habits")
        .then()
        .statusCode(BAD_REQUEST.value())
        .body("errors[0].objectName", equalTo("modificationRequest"))
        .body("errors[0].field", equalTo("schedule"))
        .body("errors[0].code", equalTo("NotNull"));

    assertThat(numberOfHabits()).isZero();
  }

  @Test
  public void deleteHabit() {

    String location = insertHabit("Delete me");

    assertThat(numberOfHabits()).isOne();

    given()
        .header(DEFAULT_AUTHORIZATION_HEADER)
        .when()
        .delete(location)
        .then()
        .statusCode(OK.value())
        .body(isEmptyOrNullString());

    assertThat(numberOfHabits()).isZero();
  }

  @Test
  public void deleteHabitNotFound() {
    given()
        .header(DEFAULT_AUTHORIZATION_HEADER)
        .when()
        .delete("/habits/{id}", 999)
        .then()
        .statusCode(NOT_FOUND.value())
        .body("message", equalTo("Habit '999' could not be found."));
  }

  private String insertHabit(String name) {

    ModificationRequest habit =
        ModificationRequest.builder().name(name).schedule(DEFAULT_SCHEDULE).build();

    return given()
        .header(DEFAULT_AUTHORIZATION_HEADER)
        .contentType(JSON)
        .body(habit)
        .when()
        .post("/habits")
        .then()
        .statusCode(CREATED.value())
        .extract()
        .header(HttpHeaders.LOCATION);
  }

  private int numberOfHabits() {
    return JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME);
  }
}
