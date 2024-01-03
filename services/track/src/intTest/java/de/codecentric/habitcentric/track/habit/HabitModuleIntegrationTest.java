package de.codecentric.habitcentric.track.habit;

import static org.assertj.core.api.Assertions.assertThat;

import de.codecentric.habitcentric.track.auth.UserIdArgumentResolver;
import java.time.LocalDate;
import java.util.Set;
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

  @Autowired private HabitTrackingController habitTrackingController;
  @Autowired private HabitTrackingRepository habitTrackingRepository;

  @AfterEach
  void tearDown() {
    habitTrackingRepository.deleteAll();
  }

  @Test
  void shouldPublishDateTrackedEventWhenHabitTrackingIsSaved(Scenario scenario) {
    habitTrackingRepository.save(
        HabitTracking.from("userId", 1L, Set.of(LocalDate.parse("2023-09-29"))));

    scenario
        .stimulate(
            () ->
                habitTrackingController.putHabitTrackingRecords(
                    "userId",
                    1L,
                    Set.of(LocalDate.parse("2023-09-29"), LocalDate.parse("2023-09-30"))))
        .andWaitForEventOfType(HabitTracking.DateTracked.class)
        .toArriveAndVerify(
            event -> {
              assertThat(event.habitId()).isEqualTo(1L);
              assertThat(event.userId()).isEqualTo("userId");
              assertThat(event.trackDate()).isEqualTo(LocalDate.parse("2023-09-30"));
            });
  }

  @Test
  void shouldPublishDateUntrackedEventWhenExistingHabitTrackingIsRemoved(Scenario scenario) {
    habitTrackingRepository.save(
        HabitTracking.from(
            "userId", 1L, Set.of(LocalDate.parse("2023-09-29"), LocalDate.parse("2023-09-30"))));

    scenario
        .stimulate(
            () ->
                habitTrackingController.putHabitTrackingRecords(
                    "userId", 1L, Set.of(LocalDate.parse("2023-09-29"))))
        .andWaitForEventOfType(HabitTracking.DateUntracked.class)
        .toArriveAndVerify(
            event -> {
              assertThat(event.habitId()).isEqualTo(1L);
              assertThat(event.userId()).isEqualTo("userId");
              assertThat(event.trackDate()).isEqualTo(LocalDate.parse("2023-09-30"));
            });
  }
}
