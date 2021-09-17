package de.codecentric.hc.report

import de.codecentric.hc.report.Frequency.DAILY
import de.codecentric.hc.report.Frequency.MONTHLY
import de.codecentric.hc.report.Frequency.WEEKLY
import de.codecentric.hc.report.ReportPeriod.LAST_30_DAYS
import de.codecentric.hc.report.ReportPeriod.LAST_7_DAYS
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class ReportPeriodTest {

    @Nested
    inner class `last 7 days` {
        @Test
        fun `should include daily and weekly frequency`() {
            assertThat(LAST_7_DAYS.includedFrequencies).containsExactlyInAnyOrder(DAILY, WEEKLY)
        }

        @Test
        fun `should return date range from today that includes 7 days`() {
            val result = LAST_7_DAYS.asDateRange(LocalDate.parse("2020-01-31"))
            assertThat(result.start).isEqualTo("2020-01-25")
            assertThat(result.count()).isEqualTo(7);
        }
    }

    @Nested
    inner class `last 30 days` {
        @Test
        fun `should include daily, weekly and monthly frequency`() {
            assertThat(LAST_30_DAYS.includedFrequencies).containsExactlyInAnyOrder(DAILY, WEEKLY, MONTHLY)
        }

        @Test
        fun `should return date range from today that includes 30 days`() {
            val result = LAST_30_DAYS.asDateRange(LocalDate.parse("2020-01-31"))
            assertThat(result.start).isEqualTo("2020-01-02")
            assertThat(result.count()).isEqualTo(30);
        }
    }
}