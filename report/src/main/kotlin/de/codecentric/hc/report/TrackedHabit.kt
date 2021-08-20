package de.codecentric.hc.report

import de.codecentric.hc.report.date.DateRange
import java.time.LocalDate
import kotlin.math.roundToInt

class TrackedHabit(val habit: Habit, val tracks: Collection<LocalDate>) {

    fun getScheduledRepetitionsForPeriod(dateRange: DateRange): Int {
        val daysInRange = dateRange.count()
        return when (habit.schedule.frequency) {
            Frequency.DAILY -> habit.schedule.repetitions * daysInRange
            Frequency.WEEKLY -> (habit.schedule.repetitions * daysInRange / 7.0).roundToInt()
            Frequency.MONTHLY -> (habit.schedule.repetitions * daysInRange / 30.0).roundToInt()
            else -> throw NotImplementedError()
        }
    }

    fun getTrackedRepetitionsForPeriod(dateRange: DateRange): Int =
        tracks.count { it in dateRange }
}