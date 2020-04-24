package de.codecentric.hc.report

import de.codecentric.hc.report.date.DateService
import de.codecentric.hc.report.date.rangeTo
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class ReportServiceTest {

    @MockK
    lateinit var dateService: DateService

    @MockK
    lateinit var trackedHabitService: TrackedHabitService

    @InjectMockKs
    lateinit var subject: ReportService

    val trackedHabits = HashSet<TrackedHabit>()

    @BeforeEach
    internal fun setUp() {
        every { dateService.today() } returns LocalDate.parse("2020-01-31")
        every {
            trackedHabitService.getTrackedHabitsWith(
                setOf(
                    Frequency.DAILY,
                    Frequency.WEEKLY,
                    Frequency.MONTHLY
                )
            )
        } returns trackedHabits
    }

    @Nested
    inner class `weekly rate` {

        @Test
        fun `given 1 habit with 2 actual repetitions and 4 scheduled repetitions, should return 0,5`() {
            trackedHabits.add(mockTrackedHabit("2020-01-25", "2020-01-31", Frequency.DAILY, 4, 2))

            val result = subject.calculateAchievementRates()

            assertThat(result.week).isEqualTo(0.5)
        }

        @Test
        fun `given 2 habits with sum of 1 actual repetition and 4 scheduled repetitions, should return 0,25`() {
            trackedHabits.add(mockTrackedHabit("2020-01-25", "2020-01-31", Frequency.DAILY, 3, 0))
            trackedHabits.add(mockTrackedHabit("2020-01-25", "2020-01-31", Frequency.WEEKLY, 1, 1))

            val result = subject.calculateAchievementRates()

            assertThat(result.week).isEqualTo(0.25)
        }

        @Test
        fun `given 1 daily, 1 weekly and 1 monthly habit, should not include monthly habit in weekly rate`() {
            trackedHabits.add(mockTrackedHabit("2020-01-25", "2020-01-31", Frequency.DAILY, 1, 1))
            trackedHabits.add(mockTrackedHabit("2020-01-25", "2020-01-31", Frequency.WEEKLY, 1, 1))
            trackedHabits.add(mockTrackedHabit("2020-01-25", "2020-01-31", Frequency.MONTHLY, 1, 0))

            val result = subject.calculateAchievementRates()

            assertThat(result.week).isEqualTo(1.0)
        }
    }

    @Nested
    inner class `monthly rate` {

        @Test
        fun `given 1 habit with 2 actual repetitions and 4 scheduled repetitions, should return 0,5`() {
            trackedHabits.add(mockTrackedHabit("2020-01-02", "2020-01-31", Frequency.DAILY, 4, 2))

            val result = subject.calculateAchievementRates()

            assertThat(result.month).isEqualTo(0.5)
        }

        @Test
        fun `given 3 habits with sum of 3 actual repetitions and 4 scheduled repetitions, should return 0,75`() {
            trackedHabits.add(mockTrackedHabit("2020-01-02", "2020-01-31", Frequency.DAILY, 2, 2))
            trackedHabits.add(mockTrackedHabit("2020-01-02", "2020-01-31", Frequency.WEEKLY, 1, 1))
            trackedHabits.add(mockTrackedHabit("2020-01-02", "2020-01-31", Frequency.MONTHLY, 1, 0))

            val result = subject.calculateAchievementRates()

            assertThat(result.month).isEqualTo(0.75)
        }
    }

    fun mockTrackedHabit(
        beginDate: String,
        endDate: String,
        frequency: Frequency,
        scheduledRepetitions: Int,
        trackedRepetitions: Int
    ): TrackedHabit {
        val trackedHabit = mockk<TrackedHabit>(relaxed = true)
        every { trackedHabit.habit.schedule.frequency } returns frequency
        every {
            trackedHabit.getTrackedRepetitionsForPeriod(
                LocalDate.parse(beginDate)..
                    LocalDate.parse(endDate)
            )
        } returns trackedRepetitions
        every {
            trackedHabit.getScheduledRepetitionsForPeriod(
                LocalDate.parse(beginDate)..
                    LocalDate.parse(endDate)
            )
        } returns scheduledRepetitions
        return trackedHabit
    }
}