package de.codecentric.hc.report

data class Habit(val id: Long, val schedule: Schedule)

data class Schedule(val repetitions: Int, val frequency: Frequency)

enum class Frequency {
    DAILY, WEEKLY, MONTHLY, YEARLY
}
