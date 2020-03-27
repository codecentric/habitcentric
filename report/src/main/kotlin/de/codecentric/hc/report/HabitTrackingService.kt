package de.codecentric.hc.report

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

@Service
class HabitTrackingService(
        val habitTrackingProperties: HabitTrackingProperties,
        val restTemplate: RestTemplate
) {

    fun getTrackingDates(): List<LocalDate> {
        val trackingDates = restTemplate.getForObject(
                habitTrackingProperties.serviceUrl, Array<LocalDate>::class.java)
        return trackingDates?.toList() ?: emptyList()
    }
}