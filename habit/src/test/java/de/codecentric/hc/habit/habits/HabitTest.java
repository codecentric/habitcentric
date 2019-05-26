package de.codecentric.hc.habit.habits;

import static de.codecentric.hc.habit.habits.Habit.Schedule.Frequency.MONTHLY;
import static org.assertj.core.api.Assertions.assertThat;

import de.codecentric.hc.habit.habits.Habit.ModificationRequest;
import de.codecentric.hc.habit.habits.Habit.Schedule;
import org.junit.Test;

public class HabitTest {

  @Test
  public void canBeCreatedFromModificationRequests() {
    String userId = "dummy";
    ModificationRequest request = new ModificationRequest("ABC", new Schedule(2, MONTHLY));
    Habit habit = Habit.from(request, userId);
    assertThat(habit.getName()).isEqualTo(request.getName());
    assertThat(habit.getSchedule()).isEqualTo(request.getSchedule());
    assertThat(habit.getUserId()).isEqualTo(userId);
  }
}
