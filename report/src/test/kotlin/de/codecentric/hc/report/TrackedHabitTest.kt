package de.codecentric.hc.report

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
                val habit = Habit(0, Schedule(1, Frequency.DAILY))
                val trackedHabit = TrackedHabit(habit, emptyList())
                val result =
                        trackedHabit.getScheduledRepetitionsForPeriod(
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 1, 7)
                        )
                assertThat(result).isEqualTo(7)
            }

            @Test
            fun `given one repetition and 30-day period, should return 30`() {
                val habit = Habit(0, Schedule(1, Frequency.DAILY))
                val trackedHabit = TrackedHabit(habit, emptyList())
                val result =
                        trackedHabit.getScheduledRepetitionsForPeriod(
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 1, 30)
                        )
                assertThat(result).isEqualTo(30)
            }

            @Test
            fun `given two repetitions and 30-day period, should return 60`() {
                val habit = Habit(0, Schedule(2, Frequency.DAILY))
                val trackedHabit = TrackedHabit(habit, emptyList())
                val result =
                        trackedHabit.getScheduledRepetitionsForPeriod(
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 1, 30)
                        )
                assertThat(result).isEqualTo(60)
            }
        }

        @Nested
        inner class `given weekly habit` {
            @Test
            fun `given one repetition and 7-day period, should return 1`() {
                val habit = Habit(0, Schedule(1, Frequency.WEEKLY))
                val trackedHabit = TrackedHabit(habit, emptyList())
                val result =
                        trackedHabit.getScheduledRepetitionsForPeriod(
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 1, 7)
                        )
                assertThat(result).isEqualTo(1)
            }

            @Test
            fun `given one repetition and 30-day period, should return 4`() {
                val habit = Habit(0, Schedule(1, Frequency.WEEKLY))
                val trackedHabit = TrackedHabit(habit, emptyList())
                val result =
                        trackedHabit.getScheduledRepetitionsForPeriod(
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 1, 30)
                        )
                assertThat(result).isEqualTo(4)
            }

            @Test
            fun `given two repetitions and 7-day period, should return 2`() {
                val habit = Habit(0, Schedule(2, Frequency.WEEKLY))
                val trackedHabit = TrackedHabit(habit, emptyList())
                val result =
                        trackedHabit.getScheduledRepetitionsForPeriod(
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 1, 7)
                        )
                assertThat(result).isEqualTo(2)
            }

            @Test
            fun `given two repetitions and 30-day period, should return 9`() {
                val habit = Habit(0, Schedule(2, Frequency.WEEKLY))
                val trackedHabit = TrackedHabit(habit, emptyList())
                val result =
                        trackedHabit.getScheduledRepetitionsForPeriod(
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 1, 30)
                        )
                assertThat(result).isEqualTo(9)
            }
        }

        @Nested
        inner class `given monthly habit` {

            @Test
            fun `given one repetition and 30-day period, should return 1`() {
                val habit = Habit(0, Schedule(1, Frequency.MONTHLY))
                val trackedHabit = TrackedHabit(habit, emptyList())
                val result =
                        trackedHabit.getScheduledRepetitionsForPeriod(
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 1, 30)
                        )
                assertThat(result).isEqualTo(1)
            }

            @Test
            fun `given one repetition and 7-day period, should return 0`() {
                val habit = Habit(0, Schedule(1, Frequency.MONTHLY))
                val trackedHabit = TrackedHabit(habit, emptyList())
                val result =
                        trackedHabit.getScheduledRepetitionsForPeriod(
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 1, 7)
                        )
                assertThat(result).isEqualTo(0)
            }

            @Test
            fun `given four repetitions and 7-day period, should return 1`() {
                val habit = Habit(0, Schedule(4, Frequency.MONTHLY))
                val trackedHabit = TrackedHabit(habit, emptyList())
                val result =
                        trackedHabit.getScheduledRepetitionsForPeriod(
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 1, 7)
                        )
                assertThat(result).isEqualTo(1)
            }

            @Test
            fun `given two repetitions and 30-day period, should return 2`() {
                val habit = Habit(0, Schedule(2, Frequency.MONTHLY))
                val trackedHabit = TrackedHabit(habit, emptyList())
                val result =
                        trackedHabit.getScheduledRepetitionsForPeriod(
                                LocalDate.of(2020, 1, 1),
                                LocalDate.of(2020, 1, 30)
                        )
                assertThat(result).isEqualTo(2)
            }
        }
    }
}

