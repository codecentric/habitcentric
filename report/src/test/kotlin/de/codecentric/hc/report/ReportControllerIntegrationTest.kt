package de.codecentric.hc.report

import de.codecentric.hc.report.api.ReportController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [ReportController::class])
internal class ReportControllerIntegrationTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    internal fun `should return report`() {
        mockMvc.perform((get("/report"))).andExpect(status().isOk())
    }
}