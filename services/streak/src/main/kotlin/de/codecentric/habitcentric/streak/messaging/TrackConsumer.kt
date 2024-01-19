package de.codecentric.habitcentric.streak.messaging

import de.codecentric.habitcentric.streak.DateTracked
import de.codecentric.habitcentric.streak.DateUntracked
import de.codecentric.habitcentric.streak.StreakService
import org.springframework.kafka.annotation.KafkaHandler
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@KafkaListener(topics = ["track-events"])
class TrackConsumer(private val streakService: StreakService) {

  @KafkaHandler
  fun consumeDateTrackedEvent(event: DateTracked) {
    streakService.on(event)
  }

  @KafkaHandler
  fun consumeDateUntrackedEvent(event: DateUntracked) {
    streakService.on(event);
  }
}
