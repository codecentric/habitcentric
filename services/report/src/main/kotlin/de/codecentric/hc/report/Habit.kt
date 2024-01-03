package de.codecentric.hc.report

import java.util.UUID

data class Habit(val id: UUID, val schedule: Schedule)

data class Schedule(val repetitions: Int, val frequency: Frequency)

enum class Frequency {
  DAILY, WEEKLY, MONTHLY, YEARLY
}
