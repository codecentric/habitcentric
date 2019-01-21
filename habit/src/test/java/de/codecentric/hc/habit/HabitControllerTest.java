package de.codecentric.hc.habit;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.isEmptyOrNullString;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HabitControllerTest {

    private static final String TABLE_NAME = "HABIT";

    @ClassRule
    public static JdbcDatabaseContainer database = new PostgreSQLContainer();

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @After
    public void cleanUp() throws SQLException {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, TABLE_NAME);
    }

    @Test
    public void getHabits() throws InterruptedException {

        String[] inserted = {"Jogging", "Play guitar", "Meditate"};
        String[] expected = {"Jogging", "Meditate", "Play guitar"};

        Stream.of(inserted).forEach(name -> insertHabit(name));

        given().port(port)
                .when().get("/habits")
                .then().statusCode(200)
                .body("name", contains(expected))
                .body("id", everyItem(greaterThan(0)));
    }

    @Test
    public void getHabitsOrderedByNameAscending() throws InterruptedException {

        String[] inserted = {"c", "ä", "d", "b", "a", "A", "ac", "s", "af", "x", "ß"};
        String[] expected = {"a", "A", "ä", "ac", "af", "b", "c", "d", "s", "ß", "x"};

        Stream.of(inserted).forEach(name -> insertHabit(name));

        given().port(port)
                .when().get("/habits")
                .then().statusCode(200)
                .body("name", contains(expected))
                .body("id", everyItem(greaterThan(0)));
    }

    @Test
    public void createHabit() {

        HabitModificationRequest body = new HabitModificationRequest("New habit");

        assertThat(numberOfHabits()).isZero();

        given().port(port).contentType(JSON).body(body)
                .when().post("/habits")
                .then().statusCode(HttpStatus.CREATED.value()).body(isEmptyOrNullString());

        assertThat(numberOfHabits()).isOne();
    }

    @Test
    public void createHabitWithBlankName() {

        HabitModificationRequest body = new HabitModificationRequest("   ");

        given().port(port).contentType(JSON).body(body)
                .when().post("/habits")
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("Please provide a valid habit name."));

        assertThat(numberOfHabits()).isZero();
    }

    @Test
    public void createHabitWithDuplicateName() {

        String habitName = "ABC";

        insertHabit(habitName);

        given().port(port).contentType(JSON).body(new HabitModificationRequest(habitName))
                .when().post("/habits")
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("Please choose a unique habit name."));

        assertThat(numberOfHabits()).isOne();
    }

    @Test
    public void createHabitWit64CharacterName() {

        String habitName = StringUtils.repeat("a", 64);

        given().port(port).contentType(JSON).body(new HabitModificationRequest(habitName))
                .when().post("/habits")
                .then().statusCode(HttpStatus.CREATED.value())
                .body(isEmptyOrNullString());

        assertThat(numberOfHabits()).isOne();
    }

    @Test
    public void createHabitWit65CharacterName() {

        String habitName = StringUtils.repeat("a", 65);

        given().port(port).contentType(JSON).body(new HabitModificationRequest(habitName))
                .when().post("/habits")
                .then().statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("This habit name is too long."));

        assertThat(numberOfHabits()).isZero();
    }

    @Test
    public void deleteHabit() {

        String location = insertHabit("Delete me");

        assertThat(numberOfHabits()).isOne();

        when().delete(location)
                .then().statusCode(HttpStatus.OK.value())
                .body(isEmptyOrNullString());

        assertThat(numberOfHabits()).isZero();
    }

    @Test
    public void deleteHabitNotFound() {
        given().port(port)
                .when().delete("/habits/{id}", 999)
                .then().statusCode(HttpStatus.NOT_FOUND.value())
                .body("message", equalTo("Habit '999' could not be found."));
    }

    private String insertHabit(String name) {
        return given().port(port).contentType(JSON).body(new HabitModificationRequest(name))
                .when().post("/habits")
                .then().statusCode(HttpStatus.CREATED.value())
                .extract().header(HttpHeaders.LOCATION);
    }

    private int numberOfHabits() {
        return JdbcTestUtils.countRowsInTable(jdbcTemplate, TABLE_NAME);
    }
}
