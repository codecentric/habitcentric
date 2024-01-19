package de.codecentric.habitcentric.streak

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StreakService(private val streakRepository: StreakRepository) {

  fun on(event: HabitCreated) {
    val streak = Streak.from(Habit.from(event.habitId, event.frequency, event.repetitions))
    streakRepository.save(streak);
  }

  fun on(event: HabitDeleted) {
    streakRepository.deleteByHabitId(event.habitId);
  }

  @Transactional
  fun on(event: DateTracked) {
    val streak = streakRepository.findByHabitId(event.habitId)
    streak.ifPresent {
      val updatedStreak = it.track(event.trackDate)
      streakRepository.save(updatedStreak)
    }
  }

  @Transactional
  fun on(event: DateUntracked) {
    val streak = streakRepository.findByHabitId(event.habitId)
    streak.ifPresent {
      val updatedStreak = it.untrack(event.trackDate)
      streakRepository.save(updatedStreak)
    }
  }
}
