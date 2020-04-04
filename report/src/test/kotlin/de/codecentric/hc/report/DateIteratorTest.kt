package de.codecentric.hc.report

import de.codecentric.hc.report.date.DateIterator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class DateIteratorTest {

    @Nested
    inner class `has next` {

        @Test
        fun `returns true when next date is before inclusive end date`() {
            val subject = createDateIterator(startDate = "2020-01-01", endDateInclusive = "2020-01-02")
            val result = subject.hasNext()
            assertThat(result).isTrue()
        }

        @Test
        fun `returns true when next date is equal to inclusive end date`() {
            val subject = createDateIterator(startDate = "2020-01-01", endDateInclusive = "2020-01-01")
            val result = subject.hasNext()
            assertThat(result).isTrue()
        }

        @Test
        fun `returns false when next date is after inclusive end date`() {
            val subject = createDateIterator(startDate = "2020-01-02", endDateInclusive = "2020-01-01")
            val result = subject.hasNext()
            assertThat(result).isFalse()
        }
    }

    @Nested
    inner class `next` {
        @Test
        fun `given first call, returns start date`() {
            val subject = createDateIterator(startDate = "2020-01-01", endDateInclusive = "2020-01-01")
            val result = subject.next()
            assertThat(result).isEqualTo(LocalDate.of(2020, 1, 1))
        }

        @Test
        fun `given day step of 1 and second call, returns day after start day`() {
            val subject = createDateIterator(startDate = "2020-01-01", endDateInclusive = "2020-01-02")
            subject.next()
            val result = subject.next()
            assertThat(result).isEqualTo(LocalDate.of(2020, 1, 2))
        }

        @Test
        fun `given day step of 2 and second call, returns two days after start day`() {
            val subject = createDateIterator(
                    startDate = "2020-01-01",
                    endDateInclusive = "2020-01-03",
                    stepDays = 2)
            subject.next()
            val result = subject.next()
            assertThat(result).isEqualTo(LocalDate.of(2020, 1, 3))
        }
    }

    fun createDateIterator(startDate: String = "2020-01-01",
                           endDateInclusive: String = "2020-01-02",
                           stepDays: Long = 1) =
            DateIterator(LocalDate.parse(startDate), LocalDate.parse(endDateInclusive), stepDays)
}