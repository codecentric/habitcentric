package de.codecentric.streak.domain

import de.codecentric.streak.domain.Habit.Frequency
import de.codecentric.streak.domain.Habit.Frequency.*
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
) : Persistable<UUID> {

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

  override fun getId() = id
  override fun isNew() = new
}

sealed interface Timeperiod {
  companion object {
    fun from(date: LocalDate, frequency: Frequency): Timeperiod = when (frequency) {
      DAILY -> Day.from(date)
      WEEKLY -> Week.from(date)
      MONTHLY -> Month.from(date)
      YEARLY -> Year.from(date)
    }
  }

  fun previous(): Timeperiod
  fun calculateTracksInPeriod(trackEntries: Set<LocalDate>): Int
}

class Day private constructor(private val date: LocalDate) : Timeperiod {
  companion object {
    fun from(date: LocalDate): Day = Day(date)
  }

  override fun previous(): Timeperiod = Day(date.minusDays(1))

  override fun calculateTracksInPeriod(trackEntries: Set<LocalDate>): Int =
    trackEntries.count { it == date }
}

class Week private constructor(private val start: LocalDate, private val end: LocalDate) :
  Timeperiod {

  companion object {
    private val WEEK_START = DayOfWeek.MONDAY
    private val WEEK_END = DayOfWeek.SUNDAY

    fun from(date: LocalDate): Week = Week(
      start = date.with(TemporalAdjusters.previousOrSame(WEEK_START)),
      end = date.with(TemporalAdjusters.nextOrSame(WEEK_END))
    )
  }

  override fun previous(): Week = Week(start.minusWeeks(1), end.minusWeeks(1))

  override fun calculateTracksInPeriod(trackEntries: Set<LocalDate>): Int =
    dates().intersect(trackEntries).size

  private fun dates(): Set<LocalDate> = (start..end).iterator().asSequence().toSet()
}

class Month private constructor(private val start: LocalDate, private val end: LocalDate) :
  Timeperiod {

  companion object {
    fun from(date: LocalDate): Month = Month(
      start = date.with(TemporalAdjusters.firstDayOfMonth()),
      end = date.with(TemporalAdjusters.lastDayOfMonth()),
    )
  }

  override fun previous(): Timeperiod = Month(start.minusMonths(1), end.minusMonths(1))

  override fun calculateTracksInPeriod(trackEntries: Set<LocalDate>): Int =
    dates().intersect(trackEntries).size

  private fun dates(): Set<LocalDate> = (start..end).iterator().asSequence().toSet()
}

class Year private constructor(private val start: LocalDate, private val end: LocalDate) :
  Timeperiod {

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

  override fun calculateTracksInPeriod(trackEntries: Set<LocalDate>): Int =
    dates().intersect(trackEntries).size

  private fun dates(): Set<LocalDate> = (start..end).iterator().asSequence().toSet()
}

operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
  object : Iterator<LocalDate> {
    private var next = this@iterator.start
    private val finalElement = this@iterator.endInclusive
    private var hasNext = !next.isAfter(this@iterator.endInclusive)

    override fun hasNext(): Boolean = hasNext

    override fun next(): LocalDate {
      val value = next

      if (value == finalElement) {
        hasNext = false
      } else {
        next = next.plusDays(1)
      }

      return value
    }
  }
