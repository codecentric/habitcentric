package de.codecentric.habitcentric.streak

import java.time.LocalDate
import java.util.UUID

data class DateUntracked(val habitId: UUID, val trackDate: LocalDate)
