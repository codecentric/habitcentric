package de.codecentric.hc.report

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class HabitTrackingServiceTest {

    @MockK
    lateinit var restTemplate: RestTemplate

    @MockK
    lateinit var properties: HabitTrackingProperties

    @InjectMockKs
    lateinit var subject: HabitTrackingService

    @BeforeEach
    internal fun setUp() {
        every { properties.serviceUrl } returns "url"
    }

    @Test
    fun `should call habit tracking endpoint with habit ID`() {
        val trackDate = LocalDate.of(2020, 1, 1)
        every { restTemplate.getForObject<Array<LocalDate>>("url/track/habits/1") } returns arrayOf(trackDate)

        val trackingDates = subject.getTrackingDates(1)

        assertThat(trackingDates).isEqualTo(listOf(trackDate))
    }
}