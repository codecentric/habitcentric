package de.codecentric.hc.report.date

import java.time.LocalDate

class DateIterator(
        startDate: LocalDate,
        val endDateInclusive: LocalDate,
        val stepDays: Long = 1) : Iterator<LocalDate> {

    var currentDate = startDate

    override fun hasNext(): Boolean = currentDate <= endDateInclusive

    override fun next(): LocalDate {
        val nextDate = currentDate
        currentDate = currentDate.plusDays(stepDays)
        return nextDate
    }
}