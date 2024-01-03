package de.codecentric.habitcentric.track.habit;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HabitTrackingTest {

  @Test
  void trackShouldAddTrackingDates() {
    var subject = HabitTracking.from("userId", 1L);
    subject.track(Set.of(LocalDate.parse("2023-01-01")));
    assertThat(subject.getTrackings()).contains(LocalDate.parse("2023-01-01"));
  }

  @Test
  void trackShouldOverwriteExistingTrackingDates() {
    var subject = HabitTracking.from("userId", 1L, List.of(LocalDate.parse("2023-01-01")));
    subject.track(Set.of(LocalDate.parse("2023-01-02")));
    assertThat(subject.getTrackings()).containsOnly(LocalDate.parse("2023-01-02"));
  }

  @Test
  void trackShouldNotAddTrackingDateIfTrackingDateAlreadyExists() {
    var subject = HabitTracking.from("userId", 1L, List.of(LocalDate.parse("2023-01-01")));
    subject.track(Set.of(LocalDate.parse("2023-01-01")));
    assertThat(subject.getTrackings()).containsOnly(LocalDate.parse("2023-01-01"));
  }

  @Test
  void getSortedTrackingDatesShouldReturnTrackingDatesSortedAscending() {
    var subject =
        HabitTracking.from(
            "userId", 1L, List.of(LocalDate.parse("2023-01-02"), LocalDate.parse("2023-01-01")));
    assertThat(subject.getSortedTrackingDates())
        .containsExactly(LocalDate.parse("2023-01-01"), LocalDate.parse("2023-01-02"));
  }

  @Nested
  class DateTrackedTest {
    @Test
    void getIdShouldReturnCombinedId() {
      var subject = new HabitTracking.DateTracked("userId", 1L, LocalDate.now());
      assertThat(subject.getId()).isEqualTo("userId-1");
    }
  }

  @Nested
  class DateUntrackedTest {
    @Test
    void getIdShouldReturnCombinedId() {
      var subject = new HabitTracking.DateUntracked("userId", 1L, LocalDate.now());
      assertThat(subject.getId()).isEqualTo("userId-1");
    }
  }
}
