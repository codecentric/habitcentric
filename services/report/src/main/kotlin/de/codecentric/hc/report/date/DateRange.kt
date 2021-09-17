package de.codecentric.hc.report.date

import java.time.LocalDate

class DateRange(
    override val start: LocalDate,
    override val endInclusive: LocalDate,
    val stepDays: Long = 1
) :
    Iterable<LocalDate>, ClosedRange<LocalDate> {

    override fun iterator(): Iterator<LocalDate> = DateIterator(start, endInclusive, stepDays)

    infix fun step(days: Long) = DateRange(start, endInclusive, days)

    override fun equals(other: Any?): Boolean =
        other is DateRange && (isEmpty() && other.isEmpty() ||
            start == other.start && endInclusive == other.endInclusive && stepDays == other.stepDays)
}