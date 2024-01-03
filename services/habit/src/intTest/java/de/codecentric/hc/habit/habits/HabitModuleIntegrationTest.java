package de.codecentric.hc.habit.habits;

import static org.assertj.core.api.Assertions.assertThat;

import de.codecentric.hc.habit.auth.UserIdArgumentResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("intTest")
@ApplicationModuleTest
@MockBean(UserIdArgumentResolver.class)
public class HabitModuleIntegrationTest {

  @Autowired private HabitController habitController;

  @Test
  void shouldPublishHabitCreatedEventWhenHabitIsCreated(Scenario scenario) {
    Habit.ModificationRequest createJoggingHabitRequest =
        Habit.ModificationRequest.builder()
            .name("Jogging")
            .schedule(
                Habit.Schedule.builder()
                    .frequency(Habit.Schedule.Frequency.WEEKLY)
                    .repetitions(3)
                    .build())
            .build();

    scenario
        .stimulate(() -> habitController.createHabit(createJoggingHabitRequest, "userId"))
        .andWaitForEventOfType(Habit.HabitCreated.class)
        .toArriveAndVerify(
            event -> {
              assertThat(event.name()).isEqualTo("Jogging");
              assertThat(event.frequency()).isEqualTo(Habit.Schedule.Frequency.WEEKLY);
              assertThat(event.repetitions()).isEqualTo(3);
            });
  }
}
