package de.codecentric.hc.report

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "report")
data class ReportProperties(var enableMonthlyRate: Boolean = false)