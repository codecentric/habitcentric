package de.codecentric.hc.report.api

import de.codecentric.hc.report.AchievementRates
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AchievementRatesExtKtTest {
    @Test
    fun `should map week rate`() {
        val result = AchievementRates(0.42, 0.0).toAchievementRate()
        assertThat(result.week).isEqualTo(42f)
    }

    @Test
    fun `should map month rate`() {
        val result = AchievementRates(0.0, 0.42).toAchievementRate()
        assertThat(result.month).isEqualTo(42f)
    }
}