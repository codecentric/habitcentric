package de.codecentric.hc.report

import de.codecentric.hc.report.date.DateService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class ReportServiceTest {

    @MockK
    lateinit var dateService: DateService

    @RelaxedMockK
    lateinit var achievementService: AchievementService

    @InjectMockKs
    lateinit var subject: ReportService

    @Test
    fun `returns achievement rate of last 7 days`() {
        every { dateService.today() } returns LocalDate.parse("2020-01-08")
        every {
            achievementService.calculateRateOfPeriod(
                LocalDate.parse("2020-01-08"),
                LocalDate.parse("2020-01-01")
            )
        } returns 42.0
        val result = subject.calculateAchievementRates()
        assertThat(result.week).isEqualTo(42.0)
    }

    @Test
    fun `returns achievement rate of last 30 days`() {
        every { dateService.today() } returns LocalDate.parse("2020-01-31")
        every {
            achievementService.calculateRateOfPeriod(
                LocalDate.parse("2020-01-31"),
                LocalDate.parse("2020-01-01")
            )
        } returns 42.0
        val result = subject.calculateAchievementRates()
        assertThat(result.month).isEqualTo(42.0)
    }
}