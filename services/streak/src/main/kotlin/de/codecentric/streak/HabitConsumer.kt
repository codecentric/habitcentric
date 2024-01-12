package de.codecentric.streak

import de.codecentric.streak.domain.Habit
import de.codecentric.streak.domain.Habit.Frequency
import de.codecentric.streak.domain.Streak
import de.codecentric.streak.domain.StreakRepository
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@KafkaListener(topics = ["habit-events"])
class HabitConsumer(private val repository: StreakRepository) {

  @KafkaHandler
  fun consumeHabitCreatedEvent(event: HabitCreated) {
    val streak = Streak.from(Habit.from(event.habitId, event.frequency, event.repetitions))
    repository.save(streak)
  }
}

data class HabitCreated(val habitId: UUID, val frequency: Frequency, val repetitions: Int)

data class HabitDeleted(val habitId: UUID)
