package de.codecentric.hc.report

import de.codecentric.hc.report.ReportPeriod.LAST_30_DAYS
import de.codecentric.hc.report.ReportPeriod.LAST_7_DAYS
import de.codecentric.hc.report.date.DateService
import org.springframework.stereotype.Service

@Service
class ReportService(
    val dateService: DateService,
    val trackedHabitService: TrackedHabitService,
    val reportProperties: ReportProperties
) {

    fun calculateAchievementRates(): AchievementRates {
        val trackedHabits =
            trackedHabitService.getTrackedHabitsWith(setOf(Frequency.DAILY, Frequency.WEEKLY, Frequency.MONTHLY))

        val weeklyAchievementRate = calculateRateFor(trackedHabits, LAST_7_DAYS)

        if (reportProperties.enableMonthlyRate) {
            val monthlyAchievementRate = calculateRateFor(trackedHabits, LAST_30_DAYS)
            return AchievementRates(weeklyAchievementRate, monthlyAchievementRate)
        } else {
            return AchievementRates(weeklyAchievementRate)
        }

    }

    private fun calculateRateFor(
        trackedHabits: Set<TrackedHabit>,
        period: ReportPeriod
    ): Double {
        val filteredHabits = trackedHabits.filter { it.habit.schedule.frequency in period.includedFrequencies }

        val today = dateService.today()
        val reportDateRange = period.asDateRange(today)
        val sumOfScheduledRepetitions =
            filteredHabits.sumBy { it.getScheduledRepetitionsForPeriod(reportDateRange) }
        val sumOfTrackedRepetitions =
            filteredHabits.sumBy { it.getTrackedRepetitionsForPeriod(reportDateRange) }

        return when {
            sumOfScheduledRepetitions == 0 -> 0.0
            else -> sumOfTrackedRepetitions.toDouble() / sumOfScheduledRepetitions.toDouble()
        }
    }
}
