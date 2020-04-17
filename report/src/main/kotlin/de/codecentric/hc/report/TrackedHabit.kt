package de.codecentric.hc.report

import de.codecentric.hc.report.date.DateRange
import java.time.LocalDate
import kotlin.math.roundToInt

class TrackedHabit(val habit: Habit, val tracks: Collection<LocalDate>) {

    fun getScheduledRepetitionsForPeriod(beginDate: LocalDate, endDate: LocalDate): Int {
        val daysInRange = DateRange(beginDate, endDate).count()
        return when (habit.schedule.frequency) {
            Frequency.DAILY -> habit.schedule.repetitions * daysInRange
            Frequency.WEEKLY -> (habit.schedule.repetitions * daysInRange / 7.0).roundToInt()
            Frequency.MONTHLY -> (habit.schedule.repetitions * daysInRange / 30.0).roundToInt()
            else -> throw NotImplementedError()
        }
    }

    fun getTrackedRepetitionsForPeriod(beginDate: LocalDate, endDate: LocalDate): Int =
        tracks.count { it in beginDate..endDate }

    /*

    Achievement Rate = Anzahl tatsächlicher Repetitions / Anzahl gewünschter Repetitions

    Anzahl tatsächlicher Repetitions = Alle Tracks von allen Habits aufsummiert

    Anzahl gewünschter Repetitions = Alle gewünschten Repetitions von allen Habits aufsummiert


    Beispielperiode: 11.04.2020 - 17.04.2020

    Wir haben zwei Habits:
        Erster Habit: Täglich einmal Joggen
            - 11.04.2020
            - 12.04.2020
            - 13.04.2020
            - 14.04.2020
            - 15.04.2020
            - 16.04.2020
            - 17.04.2020
        Zweiter Habit: Wöchentlich einmal Budget planen
        Dritter Habit: Monatlich viermal putzen

    Alles stumpf aufsummiert: 6/8


     */
}