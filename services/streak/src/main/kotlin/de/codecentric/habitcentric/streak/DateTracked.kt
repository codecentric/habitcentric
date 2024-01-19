package de.codecentric.habitcentric.streak

import java.time.LocalDate
import java.util.UUID

data class DateTracked(val habitId: UUID, val trackDate: LocalDate) {
}
