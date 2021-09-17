package de.codecentric.hc.report

import org.springframework.stereotype.Service

@Service
class TrackedHabitService(val habitService: HabitService, val habitTrackingService: HabitTrackingService) {

    fun getTrackedHabitsWith(frequencies: Set<Frequency>): Set<TrackedHabit> =
        habitService.getHabits()
            .filter { it.schedule.frequency in frequencies }
            .map { TrackedHabit(it, habitTrackingService.getTrackingDates(it.id)) }
            .toSet()
}
