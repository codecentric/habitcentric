package de.codecentric.hc.report

import de.codecentric.hc.report.date.DateRange
import de.codecentric.hc.report.date.rangeTo
import java.time.LocalDate

enum class ReportPeriod(val includedFrequencies: Set<Frequency>, private val durationInDays: Long) {
    LAST_7_DAYS(setOf(Frequency.DAILY, Frequency.WEEKLY), 7),
    LAST_30_DAYS(setOf(Frequency.DAILY, Frequency.WEEKLY, Frequency.MONTHLY), 30);

    fun asDateRange(today: LocalDate): DateRange = today.minusDays(durationInDays - 1)..today
}