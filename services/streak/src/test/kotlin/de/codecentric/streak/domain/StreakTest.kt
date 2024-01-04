package de.codecentric.streak.domain

import de.codecentric.streak.domain.Habit.Frequency.*
import de.codecentric.streak.domain.Habit.Schedule
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

class StreakTest {
  @Test
  fun `from creates new streak with given habit`() {
    val habit = HabitFixtues.ThreeTimesWeekly
    val subject = Streak.from(habit)
    subject.habit shouldBe habit
  }

  @Test
  fun `from creates new streak with length 0`() {
    val subject = Streak.from(HabitFixtues.ThreeTimesWeekly)
    subject.length() shouldBe 0
  }

  @Test
  fun `from creates new streak with new set to true`() {
    val subject = Streak.from(HabitFixtues.ThreeTimesWeekly)
    subject.isNew shouldBe true
  }

  @Test
  fun `length returns 0 if no entries have been tracked`() {
    val subject = Streak.from(HabitFixtues.ThreeTimesWeekly)
    subject.length() shouldBe 0
  }

  @Test
  fun `length returns 1 if habit has been tracked once in the current week`() {
    val clock = Clock.fixed(Instant.parse("2024-01-11T15:00:00.00Z"), ZoneId.of("UTC"))

    val subject = Streak.from(HabitFixtues.ThreeTimesWeekly)
    val result = subject.track(LocalDate.parse("2024-01-11"))
    result.length(clock) shouldBe 1
  }

  @Test
  fun `length returns 4 if three times weekly habit has been tracked four times in the current week even though repetition count is 3`() {
    val clock = Clock.fixed(Instant.parse("2024-01-11T15:00:00.00Z"), ZoneId.of("UTC"))

    val subject = Streak.from(HabitFixtues.ThreeTimesWeekly)
    val result = subject
      .track(LocalDate.parse("2024-01-11"))
      .track(LocalDate.parse("2024-01-10"))
      .track(LocalDate.parse("2024-01-09"))
      .track(LocalDate.parse("2024-01-08"))

    result.length(clock) shouldBe 4
  }

  @Test
  fun `length returns 4 if three times weekly habit has been tracked 3 times in the last week and once in the current week`() {
    val clock = Clock.fixed(Instant.parse("2024-01-11T15:00:00.00Z"), ZoneId.of("UTC"))

    val subject = Streak.from(HabitFixtues.ThreeTimesWeekly)
    val result = subject
      .track(LocalDate.parse("2024-01-11"))
      .track(LocalDate.parse("2024-01-04"))
      .track(LocalDate.parse("2024-01-03"))
      .track(LocalDate.parse("2024-01-02"))

    result.length(clock) shouldBe 4
  }


  @Test
  fun `length returns 8 if three times weekly habit has been tracked 4 times in the last week and 4 times in the current week`() {
    val clock = Clock.fixed(Instant.parse("2024-01-11T15:00:00.00Z"), ZoneId.of("UTC"))

    val subject = Streak.from(HabitFixtues.ThreeTimesWeekly)
    val result = subject
      .track(LocalDate.parse("2024-01-11"))
      .track(LocalDate.parse("2024-01-10"))
      .track(LocalDate.parse("2024-01-09"))
      .track(LocalDate.parse("2024-01-08"))
      .track(LocalDate.parse("2024-01-04"))
      .track(LocalDate.parse("2024-01-03"))
      .track(LocalDate.parse("2024-01-02"))
      .track(LocalDate.parse("2024-01-01"))

    result.length(clock) shouldBe 8
  }

  @Test
  fun `length returns 2 if once monthly habit has been tracked once in the last month and once in the current month`() {
    val clock = Clock.fixed(Instant.parse("2024-01-11T15:00:00.00Z"), ZoneId.of("UTC"))

    val subject = Streak.from(HabitFixtues.OnceMonthly)
    val result = subject
      .track(LocalDate.parse("2024-01-01"))
      .track(LocalDate.parse("2023-12-01"))

    result.length(clock) shouldBe 2
  }

  @Test
  fun `length returns 3 if twice yearly haibt has been tracked twice in the last year and once in the current year`() {
    val clock = Clock.fixed(Instant.parse("2024-01-11T15:00:00.00Z"), ZoneId.of("UTC"))

    val subject = Streak.from(HabitFixtues.TwiceYearly)
    val result = subject
      .track(LocalDate.parse("2024-01-01"))
      .track(LocalDate.parse("2023-12-01"))
      .track(LocalDate.parse("2023-01-01"))

    result.length(clock) shouldBe 3
  }

  object HabitFixtues {
    val ThreeTimesWeekly = Habit(UUID.randomUUID(), Schedule(WEEKLY, 3))
    val OnceMonthly = Habit(UUID.randomUUID(), Schedule(MONTHLY, 1))
    val TwiceYearly = Habit(UUID.randomUUID(), Schedule(YEARLY, 2))
  }
}
