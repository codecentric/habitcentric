package de.codecentric.hc.report

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class AchievementServiceTest {

    @MockK
    lateinit var habitService: HabitService

    @MockK
    lateinit var trackingService: HabitTrackingService

    @InjectMockKs
    lateinit var subject: AchievementService

    @Test
    fun `returns 100% when habit repetitions are equal to tracked repetitions in given period`() {
        every { habitService.getHabits() } returns listOf(Habit(1, Schedule(1, Frequency.WEEKLY)))
    }

    // repetitions = anzahl gewünschte repetitions in zeitraum ermitteln
    //   zeitraum:
    // tracks = anzahl tracks in zeitraum ermitteln
    // percentage im zeitraum = tracks/ repetitions

    // Angelegt: 1.4.
    // Heute: 2.4.
    // Repititions: 10

    // Statistik
    // - Zeitraum: 3.3. - 2.4. (30 Tage)
    // - Repititions (10) / 30 Tage = 0,33
    // - Track: 0
}