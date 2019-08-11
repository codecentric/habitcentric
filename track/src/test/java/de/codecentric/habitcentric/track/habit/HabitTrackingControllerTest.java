package de.codecentric.habitcentric.track.habit;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static java.time.Month.MARCH;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class HabitTrackingControllerTest {

  @MockBean private HabitTrackingRepository repository;

  private HabitTrackingController controller;

  @BeforeEach
  public void beforeEach() {
    controller = new HabitTrackingController(repository);
  }

  @Test
  public void shouldExtractDatesFromTrackRecords() {

    final String userId = "abc.def";
    final Long habitId = 123L;

    List<HabitTracking> trackRecords =
        Arrays.asList(
            new HabitTracking(userId, habitId, LocalDate.of(2019, MARCH, 21)),
            new HabitTracking(userId, habitId, LocalDate.of(2018, DECEMBER, 31)),
            new HabitTracking(userId, habitId, LocalDate.of(2019, JANUARY, 1)));

    assertThat(controller.extractDates(trackRecords))
        .containsExactly(
            LocalDate.of(2018, DECEMBER, 31),
            LocalDate.of(2019, JANUARY, 1),
            LocalDate.of(2019, MARCH, 21));
  }
}
