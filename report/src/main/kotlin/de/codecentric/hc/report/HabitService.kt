package de.codecentric.hc.report

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class HabitService(
    val habitProperties: HabitProperties,
    val restTemplate: RestTemplate
) {

    private val habitsEndpointUrl: String get() = "${habitProperties.serviceUrl}/habits"

    fun getHabits(): List<Habit> =
        restTemplate.getForObject(habitsEndpointUrl)
}