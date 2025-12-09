package de.codecentric.habitcentric.track.habit;

import static org.assertj.core.api.Assertions.assertThat;

import de.codecentric.habitcentric.track.auth.UserIdArgumentResolver;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ActiveProfiles("intTest")
@ApplicationModuleTest
public class HabitModuleIntegrationTest {

  @MockitoBean UserIdArgumentResolver userIdArgumentResolver;

  @Autowired private HabitTrackingController habitTrackingController;
  @Autowired private HabitTrackingRepository habitTrackingRepository;

  @AfterEach
  void tearDown() {
    habitTrackingRepository.deleteAll();
  }

  @Test
  void shouldPublishDateTrackedEventWhenHabitTrackingIsSaved(Scenario scenario) {
    UUID habitId = UUID.fromString("d712645f-cd4f-40c4-b171-bb2ea72d180d");
    habitTrackingRepository.save(
        HabitTracking.from("userId", habitId, Set.of(LocalDate.parse("2023-09-29"))));

    scenario
        .stimulate(
            () ->
                habitTrackingController.putHabitTrackingRecords(
                    "userId",
                    "d712645f-cd4f-40c4-b171-bb2ea72d180d",
                    Set.of(LocalDate.parse("2023-09-29"), LocalDate.parse("2023-09-30"))))
        .andWaitForEventOfType(HabitTracking.DateTracked.class)
        .toArriveAndVerify(
            event -> {
              assertThat(event.habitId()).isEqualTo(habitId);
              assertThat(event.userId()).isEqualTo("userId");
              assertThat(event.trackDate()).isEqualTo(LocalDate.parse("2023-09-30"));
            });
  }

  @Test
  void shouldPublishDateUntrackedEventWhenExistingHabitTrackingIsRemoved(Scenario scenario) {
    UUID habitId = UUID.fromString("d712645f-cd4f-40c4-b171-bb2ea72d180d");
    habitTrackingRepository.save(
        HabitTracking.from(
            "userId",
            habitId,
            Set.of(LocalDate.parse("2023-09-29"), LocalDate.parse("2023-09-30"))));

    scenario
        .stimulate(
            () ->
                habitTrackingController.putHabitTrackingRecords(
                    "userId",
                    "d712645f-cd4f-40c4-b171-bb2ea72d180d",
                    Set.of(LocalDate.parse("2023-09-29"))))
        .andWaitForEventOfType(HabitTracking.DateUntracked.class)
        .toArriveAndVerify(
            event -> {
              assertThat(event.habitId()).isEqualTo(habitId);
              assertThat(event.userId()).isEqualTo("userId");
              assertThat(event.trackDate()).isEqualTo(LocalDate.parse("2023-09-30"));
            });
  }
}
