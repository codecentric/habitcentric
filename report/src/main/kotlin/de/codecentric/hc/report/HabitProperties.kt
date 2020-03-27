package de.codecentric.hc.report

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "habit")
data class HabitProperties(val serviceUrl: String)