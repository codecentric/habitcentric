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

@ExtendWith(MockKExtension::class)
internal class HabitServiceTest {

    @MockK
    lateinit var restTemplate: RestTemplate

    @MockK
    lateinit var properties: HabitProperties

    @InjectMockKs
    lateinit var subject: HabitService

    @BeforeEach
    internal fun setUp() {
        every { properties.serviceUrl } returns "url"
    }

    @Test
    fun `should call habit endpoint`() {
        val habit = Habit(1, Schedule(1, Frequency.WEEKLY))

        every { restTemplate.getForObject<Array<Habit>>("url/habits") } returns arrayOf(habit)

        val habits = subject.getHabits()
        assertThat(habits).isEqualTo(listOf(habit))
    }
}