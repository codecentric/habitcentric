package de.codecentric.hc.report

import de.codecentric.hc.report.date.DateService
import org.springframework.stereotype.Service

@Service
class ReportService(
    val dateService: DateService,
    val achievementService: AchievementService
) {

    fun calculateAchievementRates(): AchievementRates {
        val today = dateService.today()
        return AchievementRates(
            achievementService.calculateRateOfPeriod(today, today.minusDays(7)),
            achievementService.calculateRateOfPeriod(today, today.minusDays(30))
        )
    }
}
