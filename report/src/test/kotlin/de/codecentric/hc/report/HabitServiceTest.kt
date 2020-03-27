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

@ExtendWith(MockitoExtension::class)
internal class HabitServiceTest {

    @Mock
    lateinit var restTemplate: RestTemplate

    @Mock
    lateinit var properties: HabitProperties

    @InjectMocks
    lateinit var subject: HabitService

    @BeforeEach
    internal fun setUp() {
        whenever(properties.serviceUrl).thenReturn("url")
    }

    @Test
    internal fun `should call habit endpoint`() {
        val habit = Habit(1, Schedule(1, Frequency.WEEKLY))

        whenever(restTemplate.getForObject("url", Array<Habit>::class.java))
                .thenReturn(arrayOf(habit))

        val habits = subject.getHabits()
        assertThat(habits).isEqualTo(listOf(habit))
    }

    @Test
    internal fun `should return empty list on null return value from rest template`() {
        whenever(restTemplate.getForObject("url", Array<Habit>::class.java))
                .thenReturn(null)

        val habits = subject.getHabits()
        assertThat(habits).isEqualTo(emptyList<Habit>())
    }
}