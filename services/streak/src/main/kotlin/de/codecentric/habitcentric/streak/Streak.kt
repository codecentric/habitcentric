package de.codecentric.habitcentric.streak

import de.codecentric.habitcentric.streak.Habit.Frequency
import de.codecentric.habitcentric.streak.Habit.Frequency.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Embedded
import org.springframework.data.relational.core.mapping.Table
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.util.UUID

@Table("habits")
data class Habit(
  @Id private val id: UUID,
  @Embedded(onEmpty = Embedded.OnEmpty.USE_EMPTY) val schedule: Schedule,
  @Value("null") @Transient private val new: Boolean = false
) : Persistable<UUID> {
  companion object {
    fun from(id: UUID, frequency: Frequency, repetitions: Int): Habit =
      Habit(id, Schedule(frequency, repetitions), true)
  }

  override fun getId() = id
  override fun isNew() = new

  data class Schedule(val frequency: Frequency, val repetitions: Int)
  enum class Frequency {
    DAILY, WEEKLY, MONTHLY, YEARLY
  }
}


@Table("streaks")
data class Streak(
  @Id private val id: UUID,
  val habit: Habit,
  private val trackEntries: Set<LocalDate> = emptySet(),
  @Value("null") @Transient private val new: Boolean = false
) : AggregateRoot<UUID> {

  companion object {
    fun from(habit: Habit): Streak =
      Streak(
        id = UUID.randomUUID(),
        habit = habit,
        new = true
      )
  }

  fun length(clock: Clock = Clock.systemUTC()): Int {
    val today = LocalDate.now(clock)

    var period = Timeperiod.from(today, habit.schedule.frequency)
    var sum = 0
    do {
      sum += period.calculateTracksInPeriod(trackEntries)
      period = period.previous()
    } while (period.calculateTracksInPeriod(trackEntries) >= habit.schedule.repetitions)

    return sum
  }

  fun track(track: LocalDate): Streak = copy(trackEntries = trackEntries + track)
  fun untrack(track: LocalDate): Streak = copy(trackEntries = trackEntries - track)

  override fun saved(): AggregateRoot<UUID> = copy(new = false)

  override fun getId() = id
  override fun isNew() = new
}

sealed class Timeperiod(protected val start: LocalDate, protected val end: LocalDate) {
  companion object {
    fun from(date: LocalDate, frequency: Frequency): Timeperiod = when (frequency) {
      DAILY -> Day.from(date)
      WEEKLY -> Week.from(date)
      MONTHLY -> Month.from(date)
      YEARLY -> Year.from(date)
    }
  }

  abstract fun previous(): Timeperiod

  fun calculateTracksInPeriod(trackEntries: Set<LocalDate>): Int =
    trackEntries.count { it in start..end }
}

class Day private constructor(start: LocalDate, end: LocalDate) : Timeperiod(start, end) {
  companion object {
    fun from(date: LocalDate): Day = Day(start = date, end = date)
  }

  override fun previous(): Timeperiod = Day(start.minusDays(1), end.minusDays(1))
}

class Week private constructor(start: LocalDate, end: LocalDate) :
  Timeperiod(start, end) {

  companion object {
    private val WEEK_START = DayOfWeek.MONDAY
    private val WEEK_END = DayOfWeek.SUNDAY

    fun from(date: LocalDate): Week = Week(
      start = date.with(TemporalAdjusters.previousOrSame(WEEK_START)),
      end = date.with(TemporalAdjusters.nextOrSame(WEEK_END))
    )
  }

  override fun previous(): Week = Week(start.minusWeeks(1), end.minusWeeks(1))
}

class Month private constructor(start: LocalDate, end: LocalDate) :
  Timeperiod(start, end) {

  companion object {
    fun from(date: LocalDate): Month = Month(
      start = date.with(TemporalAdjusters.firstDayOfMonth()),
      end = date.with(TemporalAdjusters.lastDayOfMonth()),
    )
  }

  override fun previous(): Timeperiod = Month(start.minusMonths(1), end.minusMonths(1))
}

class Year private constructor(start: LocalDate, end: LocalDate) :
  Timeperiod(start, end) {

  companion object {
    fun from(date: LocalDate): Year = Year(
      start = date.with(TemporalAdjusters.firstDayOfYear()),
      end = date.with(TemporalAdjusters.lastDayOfYear())
    )
  }

  override fun previous(): Timeperiod = Year(
    start = start.minusYears(1).with(TemporalAdjusters.firstDayOfYear()),
    end = start.minusYears(1).with(TemporalAdjusters.lastDayOfYear())
  )
}
