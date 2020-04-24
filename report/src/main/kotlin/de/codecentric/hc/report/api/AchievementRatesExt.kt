package de.codecentric.hc.report.api

import de.codecentric.hc.report.AchievementRates
import de.codecentric.hc.report.api.model.AchievementRate

fun AchievementRates.toAchievementRate(): AchievementRate = AchievementRate(
    (this.week * 100).toFloat(),
    (this.month * 100).toFloat()
)