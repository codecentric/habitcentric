package de.codecentric.streak.domain

import de.codecentric.streak.DataJdbcConfiguration
import de.codecentric.streak.domain.Habit.Frequency.WEEKLY
import de.codecentric.streak.domain.Habit.Schedule
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
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
  fun `loads streak from database by habit id`() {
    val id = UUID.randomUUID()
    val streak = Streak.from(Habit(id, Schedule(WEEKLY, 3)))
    subject.save(streak)
    subject.findByHabitId(id);
  }
}
