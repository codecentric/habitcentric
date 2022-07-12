package de.codecentric.hc.report

import com.ninjasquad.springmockk.MockkBean
import de.codecentric.hc.report.api.ReportController
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(controllers = [ReportController::class])
internal class ReportControllerIntegrationTest {

    @MockkBean
    lateinit var reportService: ReportService

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `should return report`() {
        every { reportService.calculateAchievementRates() } returns AchievementRates(0.42, 0.42)

        mockMvc.get("/report/achievement").andExpect {
            status { isOk() }
            jsonPath("$.week") { value(42.0) }
        }
    }
}
