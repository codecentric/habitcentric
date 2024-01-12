package de.codecentric.habitcentric.streak

import de.codecentric.habitcentric.streak.Habit.Frequency.MONTHLY
import de.codecentric.habitcentric.streak.Habit.Frequency.WEEKLY
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.modulith.test.ApplicationModuleTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.shaded.org.awaitility.Awaitility.await
import java.util.UUID

@ActiveProfiles("intTest")
@DirtiesContext
@EmbeddedKafka(topics = ["habit-events", "habit-deleted"])
@ApplicationModuleTest
class StreakServiceIntegrationTest(
  @Autowired private val habitCreatedKafkaTemplate: KafkaTemplate<String, HabitCreated>,
  @Autowired private val habitDeletedKafkaTemplate: KafkaTemplate<String, HabitDeleted>,
  @Autowired private val streakRepository: StreakRepository,
) {

  @Test
  fun `should create a streak when HabitCreated event is received`() {
    val id = UUID.randomUUID()

    habitCreatedKafkaTemplate.send("habit-events", "key", HabitCreated(id, WEEKLY, 3))

    await().untilAsserted {
      val result = streakRepository.findByHabitId(id)
      result.asClue {
        it.isPresent shouldBe true
        it.get().habit.id shouldBe id
      }
    }
  }

  @Test
  fun `should delete streak when HabitDeleted is received`() {
    val id = UUID.randomUUID()

    streakRepository.save(Streak.from(Habit.from(id, MONTHLY, 2)))
    habitDeletedKafkaTemplate.send("habit-events", "key", HabitDeleted(id))

    await().untilAsserted {
      val result = streakRepository.findByHabitId(id)
      result.asClue {
        it.isPresent shouldBe false
      }
    }
  }
}
