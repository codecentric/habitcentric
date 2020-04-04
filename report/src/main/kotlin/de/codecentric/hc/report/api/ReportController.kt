package de.codecentric.hc.report.api

import de.codecentric.hc.report.api.model.AchievementRate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ReportController : ReportsApi {

    override fun reportAchievementGet(): ResponseEntity<AchievementRate> {
        val achievementRate = AchievementRate(42f, 42f)
        return ResponseEntity.ok(achievementRate)
    }
}