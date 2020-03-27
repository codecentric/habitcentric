package de.codecentric.hc.report

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "track")
data class HabitTrackingProperties(val serviceUrl: String)