package de.codecentric.habitcentric.streak

import de.codecentric.habitcentric.streak.database.DataJdbcConfiguration
import de.codecentric.habitcentric.streak.Habit.Frequency.MONTHLY
import de.codecentric.habitcentric.streak.Habit.Frequency.WEEKLY
import de.codecentric.habitcentric.streak.Habit.Schedule
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.equals.shouldNotBeEqual
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDate
import java.util.UUID

@ActiveProfiles("intTest")
@DataJdbcTest
@Import(DataJdbcConfiguration::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StreakRepositoryIntegrationTest(@Autowired val subject: StreakRepository) {

  @Test
  fun `saves streak to database`() {
    val streak = Streak.from(Habit(UUID.randomUUID(), Schedule(WEEKLY, 3)))
    subject.save(streak)
    subject.findById(streak.id)
  }

  @Test
  fun `updates streak in database`() {
    val streak = Streak.from(Habit(UUID.randomUUID(), Schedule(WEEKLY, 3)))

    val updatedStreak = subject.save(streak).run { track(LocalDate.parse("2024-01-11")) }
    subject.save(updatedStreak)

    val result = subject.findById(streak.id).orElseThrow()
    result shouldNotBeEqual streak
    result shouldBeEqual updatedStreak
  }

  @Test
  fun `loads streak from database by habit id`() {
    val id = UUID.randomUUID()
    val streak = Streak.from(Habit(id, Schedule(WEEKLY, 3)))
    subject.save(streak)
    subject.findByHabitId(id)
  }

  @Test
  fun `deletes streak from database by habit id`() {
    val id = UUID.randomUUID()
    val streak = Streak.from(Habit(id, Schedule(MONTHLY, 1)))
    subject.save(streak)
    subject.deleteByHabitId(id)
    subject.findByHabitId(id)
  }
}
