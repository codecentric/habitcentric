package de.codecentric.hc.report.date

import java.time.LocalDate

operator fun LocalDate.rangeTo(other: LocalDate) = DateRange(this, other)