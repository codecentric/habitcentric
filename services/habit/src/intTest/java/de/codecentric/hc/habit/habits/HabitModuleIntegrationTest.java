package de.codecentric.hc.habit.habits;

import static de.codecentric.hc.habit.habits.Habit.Schedule.Frequency.WEEKLY;
import static org.assertj.core.api.Assertions.assertThat;

import de.codecentric.hc.habit.auth.UserIdArgumentResolver;
import de.codecentric.hc.habit.habits.Habit.ModificationRequest;
import org.junit.jupiter.api.AfterEach;
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
  @Autowired private HabitRepository habitRepository;

  @AfterEach
  void tearDown() {
    habitRepository.deleteAll();
  }

  @Test
  void shouldPublishHabitCreatedEventWhenHabitIsCreated(Scenario scenario) {
    ModificationRequest createJoggingHabitRequest =
        ModificationRequest.builder()
            .name("Jogging")
            .schedule(Habit.Schedule.builder().frequency(WEEKLY).repetitions(3).build())
            .build();

    scenario
        .stimulate(() -> habitController.createHabit(createJoggingHabitRequest, "userId"))
        .andWaitForEventOfType(Habit.HabitCreated.class)
        .toArriveAndVerify(
            event -> {
              assertThat(event.name()).isEqualTo("Jogging");
              assertThat(event.frequency()).isEqualTo(WEEKLY);
              assertThat(event.repetitions()).isEqualTo(3);
            });
  }

  @Test
  void shouldPublishHabitDeletedEventWhenHabitIsDeleted(Scenario scenario) {
    Habit habit =
        Habit.from(
            ModificationRequest.builder()
                .name("Jogging")
                .schedule(Habit.Schedule.builder().frequency(WEEKLY).repetitions(3).build())
                .build(),
            "userId");
    habitRepository.save(habit);

    scenario
        .stimulate(() -> habitController.deleteHabit(habit.getId().toString(), "userId"))
        .andWaitForEventOfType(Habit.HabitDeleted.class)
        .toArriveAndVerify(
            event -> {
              assertThat(event.habitId()).isEqualTo(habit.getId());
            });
  }
}
