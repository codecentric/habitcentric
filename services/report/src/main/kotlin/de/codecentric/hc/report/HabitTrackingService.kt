package de.codecentric.hc.report

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.time.LocalDate
import java.util.UUID

@Service
class HabitTrackingService(
  val habitTrackingProperties: HabitTrackingProperties,
  val restTemplate: RestTemplate
) {

  private val trackEndpointUrl: String get() = "${habitTrackingProperties.serviceUrl}/track"

  fun getTrackingDates(habitId: UUID): List<LocalDate> =
    restTemplate.getForObject<Array<LocalDate>>("$trackEndpointUrl/habits/$habitId").asList()
}
