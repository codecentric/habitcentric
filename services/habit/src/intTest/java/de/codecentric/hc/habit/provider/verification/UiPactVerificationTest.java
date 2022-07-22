package de.codecentric.hc.habit.provider.verification;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import de.codecentric.hc.habit.habits.Habit.Schedule.Frequency;
import org.apache.http.HttpRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
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
    jdbcTemplate.update(INSERT_STATEMENT, 1, "Jogging", 2, Frequency.WEEKLY.name(), USER_ID);
    jdbcTemplate.update(INSERT_STATEMENT, 51, "Play guitar", 5, Frequency.MONTHLY.name(), USER_ID);
    jdbcTemplate.update(INSERT_STATEMENT, 101, "Meditate", 1, Frequency.DAILY.name(), USER_ID);
  }

  @State("habit with id '123' exists")
  public void createHabit123() {
    jdbcTemplate.update(INSERT_STATEMENT, 123, "Habit name", 1, Frequency.DAILY.name(), USER_ID);
  }
}
