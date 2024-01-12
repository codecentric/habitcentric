package de.codecentric.habitcentric.streak

import java.util.UUID

data class HabitCreated(val habitId: UUID, val frequency: Habit.Frequency, val repetitions: Int)
