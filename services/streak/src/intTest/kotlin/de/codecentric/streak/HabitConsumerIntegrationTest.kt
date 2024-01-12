package de.codecentric.streak

import de.codecentric.streak.domain.Habit.Frequency.WEEKLY
import de.codecentric.streak.domain.StreakRepository
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.shaded.org.awaitility.Awaitility
import java.util.Optional
import java.util.UUID
import java.util.concurrent.TimeUnit.SECONDS


@ActiveProfiles("intTest")
@SpringBootTest(classes = [HabitConsumer::class, StreakRepository::class, DataJdbcConfiguration::class])
@EnableAutoConfiguration
@DirtiesContext
@EmbeddedKafka(topics = ["habit-events", "habit-deleted"])
class HabitConsumerIntegrationTest {

  @Autowired
  private lateinit var habitCreatedKafkaTemplate: KafkaTemplate<String, HabitCreated>

  @Autowired
  private lateinit var habitDeletedKafkaTemplate: KafkaTemplate<String, HabitDeleted>

  @Autowired
  private lateinit var streakRepository: StreakRepository

  @Test
  fun createsAndSavesStreakWhenHabitWasCreated() {
    val id = UUID.randomUUID()
    habitCreatedKafkaTemplate.send("habit-events", "key", HabitCreated(id, WEEKLY, 3))
    habitCreatedKafkaTemplate.flush()

    Awaitility.await().atMost(1, SECONDS).untilAsserted {
      val result = streakRepository.findByHabitId(id)
      result.asClue {
        result.isPresent shouldBe true
        result.get().habit.id shouldBe id
      }
    }
  }

  @Test
  fun deletesStreakWhenHabitWasDeleted() {
    val id = UUID.randomUUID()
    habitDeletedKafkaTemplate.send("habit-deleted", "key", HabitDeleted(id))

    Awaitility.await().atMost(1, SECONDS).untilAsserted {
      val result = streakRepository.findByHabitId(id)
      result shouldBe Optional.empty()
    }
  }
}
