package de.codecentric.hc.habit;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HabitControllerTests {

    @ClassRule
    public static JdbcDatabaseContainer database = new PostgreSQLContainer();

    @LocalServerPort
    private int port;

    @Test
    public void getHabits() throws InterruptedException {
        assertThat(given().port(port)
                .when().get("/habits")
                .then().statusCode(200).extract().body().as(Habit[].class)
        ).containsExactlyInAnyOrder(
                new Habit(1L, "Jogging"),
                new Habit(2L, "Play guitar"),
                new Habit(3L, "Meditate")
        );
    }
}
