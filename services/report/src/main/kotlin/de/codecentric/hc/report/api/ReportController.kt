package de.codecentric.hc.report.api

import de.codecentric.hc.report.ReportService
import de.codecentric.hc.report.api.model.AchievementRate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ReportController(val reportService: ReportService) : ReportApi {

    override fun reportAchievementGet(): ResponseEntity<AchievementRate> {
        val achievementRate = reportService.calculateAchievementRates().toAchievementRate()
        return ResponseEntity.ok(achievementRate)
    }
}
