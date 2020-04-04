package de.codecentric.hc.report.date

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LocalDateExtKtTest {
    @Test
    fun `date range should contain every date`() {
        val result = LocalDate.of(2020, 1, 1)..LocalDate.of(2020, 1, 3)
        assertThat(result).containsExactly(
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 1, 2),
                LocalDate.of(2020, 1, 3)
        )
    }
}