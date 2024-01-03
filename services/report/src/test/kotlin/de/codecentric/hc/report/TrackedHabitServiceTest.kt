package de.codecentric.hc.report

import de.codecentric.hc.report.Frequency.DAILY
import de.codecentric.hc.report.Frequency.MONTHLY
import de.codecentric.hc.report.Frequency.WEEKLY
import de.codecentric.hc.report.Frequency.YEARLY
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate
import java.util.UUID

@ExtendWith(MockKExtension::class)
internal class TrackedHabitServiceTest {

  @MockK
  lateinit var habitService: HabitService

  @MockK
  lateinit var habitTrackingService: HabitTrackingService

  @InjectMockKs
  lateinit var subject: TrackedHabitService

  @Nested
  inner class `given 1 habit of each schedule frequency with 1 track each` {

    @BeforeEach
    internal fun setUp() {
      every { habitService.getHabits() } returns listOf(
        Habit(UUID.randomUUID(), Schedule(1, DAILY)),
        Habit(UUID.randomUUID(), Schedule(1, WEEKLY)),
        Habit(UUID.randomUUID(), Schedule(1, MONTHLY)),
        Habit(UUID.randomUUID(), Schedule(1, YEARLY))
      )
      every { habitTrackingService.getTrackingDates(any()) } returns listOf(LocalDate.parse("2020-01-01"))
    }

    @Test
    internal fun `given daily frequency, return 1 tracked habit with one track`() {
      val trackedHabits = subject.getTrackedHabitsWith(setOf(DAILY))
      assertThat(trackedHabits.size).isEqualTo(1)
      assertThat(trackedHabits.first().tracks.size).isEqualTo(1)
    }

    @Test
    internal fun `given daily, weekly and monthly frequency, return 3 tracked habits with one track each`() {
      val trackedHabits = subject.getTrackedHabitsWith(setOf(DAILY, WEEKLY, MONTHLY))
      assertThat(trackedHabits.size).isEqualTo(3)
      assertThat(trackedHabits).allMatch { it.tracks.size == 1 }
    }
  }
}
