package de.codecentric.hc.report

import de.codecentric.hc.report.date.rangeTo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

class TrackedHabitTest {

    @Nested
    inner class `get scheduled repetitions for period` {

        @Nested
        inner class `given daily habit` {

            @Test
            fun `given one repetition and 7-day period, should return 7`() {
                val trackedHabit = createTrackedHabit(1, Frequency.DAILY)
                val result =
                    trackedHabit.getScheduledRepetitionsForPeriod(
                        LocalDate.of(2020, 1, 1)..
                            LocalDate.of(2020, 1, 7)
                    )
                assertThat(result).isEqualTo(7)
            }

            @Test
            fun `given one repetition and 30-day period, should return 30`() {
                val trackedHabit = createTrackedHabit(1, Frequency.DAILY)
                val result =
                    trackedHabit.getScheduledRepetitionsForPeriod(
                        LocalDate.of(2020, 1, 1)..
                            LocalDate.of(2020, 1, 30)
                    )
                assertThat(result).isEqualTo(30)
            }

            @Test
            fun `given two repetitions and 30-day period, should return 60`() {
                val trackedHabit = createTrackedHabit(2, Frequency.DAILY)
                val result =
                    trackedHabit.getScheduledRepetitionsForPeriod(
                        LocalDate.of(2020, 1, 1)..
                            LocalDate.of(2020, 1, 30)
                    )
                assertThat(result).isEqualTo(60)
            }
        }

        @Nested
        inner class `given weekly habit` {
            @Test
            fun `given one repetition and 7-day period, should return 1`() {
                val trackedHabit = createTrackedHabit(1, Frequency.WEEKLY)
                val result =
                    trackedHabit.getScheduledRepetitionsForPeriod(
                        LocalDate.of(2020, 1, 1)..
                            LocalDate.of(2020, 1, 7)
                    )
                assertThat(result).isEqualTo(1)
            }

            @Test
            fun `given one repetition and 30-day period, should return 4`() {
                val trackedHabit = createTrackedHabit(1, Frequency.WEEKLY)
                val result =
                    trackedHabit.getScheduledRepetitionsForPeriod(
                        LocalDate.of(2020, 1, 1)..
                            LocalDate.of(2020, 1, 30)
                    )
                assertThat(result).isEqualTo(4)
            }

            @Test
            fun `given two repetitions and 7-day period, should return 2`() {
                val trackedHabit = createTrackedHabit(2, Frequency.WEEKLY)
                val result =
                    trackedHabit.getScheduledRepetitionsForPeriod(
                        LocalDate.of(2020, 1, 1)..
                            LocalDate.of(2020, 1, 7)
                    )
                assertThat(result).isEqualTo(2)
            }

            @Test
            fun `given two repetitions and 30-day period, should return 9`() {
                val trackedHabit = createTrackedHabit(2, Frequency.WEEKLY)
                val result =
                    trackedHabit.getScheduledRepetitionsForPeriod(
                        LocalDate.of(2020, 1, 1)..
                            LocalDate.of(2020, 1, 30)
                    )
                assertThat(result).isEqualTo(9)
            }
        }

        @Nested
        inner class `given monthly habit` {

            @Test
            fun `given one repetition and 30-day period, should return 1`() {
                val trackedHabit = createTrackedHabit(1, Frequency.MONTHLY)
                val result =
                    trackedHabit.getScheduledRepetitionsForPeriod(
                        LocalDate.of(2020, 1, 1)..
                            LocalDate.of(2020, 1, 30)
                    )
                assertThat(result).isEqualTo(1)
            }

            @Test
            fun `given one repetition and 7-day period, should return 0`() {
                val trackedHabit = createTrackedHabit(1, Frequency.MONTHLY)
                val result =
                    trackedHabit.getScheduledRepetitionsForPeriod(
                        LocalDate.of(2020, 1, 1)..
                            LocalDate.of(2020, 1, 7)
                    )
                assertThat(result).isEqualTo(0)
            }

            @Test
            fun `given four repetitions and 7-day period, should return 1`() {
                val trackedHabit = createTrackedHabit(4, Frequency.MONTHLY)
                val result =
                    trackedHabit.getScheduledRepetitionsForPeriod(
                        LocalDate.of(2020, 1, 1)..
                            LocalDate.of(2020, 1, 7)
                    )
                assertThat(result).isEqualTo(1)
            }

            @Test
            fun `given two repetitions and 30-day period, should return 2`() {
                val trackedHabit = createTrackedHabit(2, Frequency.MONTHLY)
                val result =
                    trackedHabit.getScheduledRepetitionsForPeriod(
                        LocalDate.of(2020, 1, 1)..
                            LocalDate.of(2020, 1, 30)
                    )
                assertThat(result).isEqualTo(2)
            }
        }
    }

    @Nested
    inner class `get tracked repetitions for period` {

        @Test
        internal fun `given one track is in date range, should return 1`() {
            val trackedHabit = createTrackedHabit(tracks = listOf(LocalDate.parse("2020-01-05")))

            val result = trackedHabit.getTrackedRepetitionsForPeriod(
                LocalDate.parse("2020-01-04")..
                    LocalDate.parse("2020-01-06")
            )
            assertThat(result).isEqualTo(1)
        }

        @Test
        internal fun `given three tracks are in date range, should return 3`() {
            val trackedHabit = createTrackedHabit(
                tracks = listOf(
                    LocalDate.parse("2020-01-05"),
                    LocalDate.parse("2020-01-06"),
                    LocalDate.parse("2020-01-07")
                )
            )

            val result = trackedHabit.getTrackedRepetitionsForPeriod(
                LocalDate.parse("2020-01-04")..LocalDate.parse("2020-01-07")
            )
            assertThat(result).isEqualTo(3)
        }

        @Test
        internal fun `given two of three tracks are in date range, should return 2`() {
            val trackedHabit = createTrackedHabit(
                tracks = listOf(
                    LocalDate.parse("2020-01-01"),
                    LocalDate.parse("2020-01-06"),
                    LocalDate.parse("2020-01-07")
                )
            )

            val result = trackedHabit.getTrackedRepetitionsForPeriod(
                LocalDate.parse("2020-01-04")..
                    LocalDate.parse("2020-01-07")
            )
            assertThat(result).isEqualTo(2)
        }
    }

    fun createTrackedHabit(
        repetitions: Int = 1,
        frequency: Frequency = Frequency.DAILY,
        tracks: Collection<LocalDate> = emptyList()
    ) =
        TrackedHabit(Habit(0, Schedule(repetitions, frequency)), tracks)
}

