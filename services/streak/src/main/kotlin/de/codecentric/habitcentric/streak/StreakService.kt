package de.codecentric.habitcentric.streak

import org.springframework.stereotype.Service

@Service
class StreakService(private val streakRepository: StreakRepository) {

  fun on(event: HabitCreated) {
    val streak = Streak.from(Habit.from(event.habitId, event.frequency, event.repetitions))
    streakRepository.save(streak);
  }

  fun on(event: HabitDeleted) {
    streakRepository.deleteByHabitId(event.habitId);
  }
}
