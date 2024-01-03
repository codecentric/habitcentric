package de.codecentric.hc.habit.provider.verification;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import de.codecentric.hc.habit.habits.Habit.Schedule.Frequency;
import java.util.UUID;
import org.apache.hc.core5.http.HttpRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("intTest")
@ExtendWith(PactVerificationInvocationContextProvider.class)
@PactFolder("pacts")
@Provider("hc-habit")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UiPactVerificationTest {

  @LocalServerPort protected int port;

  private static final String USER_ID = "default";
  private static final String TABLE_NAME = "hc_habit.HABIT";
  private static final String INSERT_STATEMENT =
      "INSERT INTO hc_habit.habit (id, name, repetitions, frequency, user_id) VALUES (?, ? , ?, ?, ?)";

  @Autowired private JdbcTemplate jdbcTemplate;

  @TestTemplate
  @ExtendWith(PactVerificationInvocationContextProvider.class)
  void pactVerificationTestTemplate(PactVerificationContext context, HttpRequest request) {
    request.addHeader("X-User-ID", USER_ID);
    context.verifyInteraction();
  }

  @BeforeEach
  void before(PactVerificationContext context) {
    context.setTarget(new HttpTestTarget("localhost", port, "/"));
  }

  @AfterEach
  public void cleanUp() {
    JdbcTestUtils.deleteFromTables(jdbcTemplate, TABLE_NAME);
  }

  @State("habits 'Jogging', 'Meditate' and 'Play guitar' exist")
  public void createHabitsJoggingAndMeditateAndPlayGuitar() {
    jdbcTemplate.update(
        INSERT_STATEMENT, UUID.randomUUID(), "Jogging", 2, Frequency.WEEKLY.name(), USER_ID);
    jdbcTemplate.update(
        INSERT_STATEMENT, UUID.randomUUID(), "Play guitar", 5, Frequency.MONTHLY.name(), USER_ID);
    jdbcTemplate.update(
        INSERT_STATEMENT, UUID.randomUUID(), "Meditate", 1, Frequency.DAILY.name(), USER_ID);
  }

  @State("habit with id 'd712645f-cd4f-40c4-b171-bb2ea72d180d' exists")
  public void createHabit123() {
    jdbcTemplate.update(
        INSERT_STATEMENT,
        UUID.fromString("d712645f-cd4f-40c4-b171-bb2ea72d180d"),
        "Habit name",
        1,
        Frequency.DAILY.name(),
        USER_ID);
  }
}
