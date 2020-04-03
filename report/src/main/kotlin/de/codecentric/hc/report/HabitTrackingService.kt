package de.codecentric.hc.report

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.time.LocalDate

@Service
class HabitTrackingService(
        val habitTrackingProperties: HabitTrackingProperties,
        val restTemplate: RestTemplate) {

    fun getTrackingDates(habitId: Long): List<LocalDate> =
            restTemplate.getForObject("${habitTrackingProperties.serviceUrl}/habits/${habitId}")
}