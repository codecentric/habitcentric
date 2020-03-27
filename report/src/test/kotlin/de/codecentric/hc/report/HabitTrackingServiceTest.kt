package de.codecentric.hc.report

import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.web.client.RestTemplate
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
internal class HabitTrackingServiceTest {

    @Mock
    lateinit var restTemplate: RestTemplate

    @Mock
    lateinit var properties: HabitTrackingProperties

    @InjectMocks
    lateinit var subject: HabitTrackingService

    @BeforeEach
    internal fun setUp() {
        whenever(properties.serviceUrl).thenReturn("url")
    }

    @Test
    fun `should call habit tracking endpoint`() {
        val trackDate = LocalDate.of(2020, 1, 1)
        whenever(restTemplate.getForObject("url", Array<LocalDate>::class.java))
                .thenReturn(arrayOf(trackDate))

        val trackingDates = subject.getTrackingDates()

        assertThat(trackingDates).isEqualTo(listOf(trackDate))
    }

    @Test
    fun `should return empty list on null return value from rest template`() {
        whenever(restTemplate.getForObject("url", Array<LocalDate>::class.java))
                .thenReturn(null)
        val trackingDates = subject.getTrackingDates()
        assertThat(trackingDates).isEqualTo(emptyList<LocalDate>())
    }
}