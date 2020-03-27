package de.codecentric.hc.report

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class HabitService(val habitProperties: HabitProperties, val restTemplate: RestTemplate) {

    fun getHabits(): List<Habit> {
        val habits = restTemplate.getForObject(habitProperties.serviceUrl, Array<Habit>::class.java);
        return habits?.toList() ?: emptyList()
    }
}