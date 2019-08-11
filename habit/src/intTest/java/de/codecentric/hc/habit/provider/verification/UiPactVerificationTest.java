package de.codecentric.hc.habit.provider.verification;

import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.TargetRequestFilter;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.SpringRestPactRunner;
import au.com.dius.pact.provider.spring.target.SpringBootHttpTarget;
import de.codecentric.hc.habit.habits.Habit.Schedule.Frequency;
import org.apache.http.HttpRequest;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@ActiveProfiles("intTest")
@RunWith(SpringRestPactRunner.class)
@PactFolder("pacts")
@Provider("hc-habit")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UiPactVerificationTest {

  private static final String USER_ID = "default";
  private static final String TABLE_NAME = "hc_habit.HABIT";
  private static final String INSERT_STATEMENT =
      "INSERT INTO hc_habit.habit (id, name, repetitions, frequency, user_id) VALUES (?, ? , ?, ?, ?)";

  @ClassRule public static final JdbcDatabaseContainer DATABASE = new PostgreSQLContainer();

  @Autowired private JdbcTemplate jdbcTemplate;

  @TestTarget public final Target target = new SpringBootHttpTarget();

  @After
  public void cleanUp() {
    JdbcTestUtils.deleteFromTables(jdbcTemplate, TABLE_NAME);
  }

  @TargetRequestFilter
  public void exampleRequestFilter(HttpRequest request) {
    request.addHeader("X-User-ID", USER_ID);
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
