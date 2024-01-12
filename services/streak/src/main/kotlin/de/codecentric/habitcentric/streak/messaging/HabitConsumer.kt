package de.codecentric.habitcentric.streak.messaging

import de.codecentric.habitcentric.streak.HabitCreated
import de.codecentric.habitcentric.streak.HabitDeleted
import de.codecentric.habitcentric.streak.StreakService
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@KafkaListener(topics = ["habit-events"])
class HabitConsumer(
  private val streakService: StreakService
) {

  @KafkaHandler
  fun consumeHabitCreatedEvent(event: HabitCreated) {
    streakService.on(event)
  }

  @KafkaHandler
  fun consumeHabitDeletedEvent(event: HabitDeleted) {
    streakService.on(event)
  }
}
