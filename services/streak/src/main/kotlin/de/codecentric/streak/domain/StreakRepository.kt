package de.codecentric.streak.domain

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface StreakRepository : CrudRepository<Streak, UUID> {

  @Query(
    """
    SELECT s.id, h.id habit_id, h.frequency habit_frequency, h.repetitions habit_repetitions
      FROM hc_streak.streaks s
      INNER JOIN hc_streak.habits h
        ON s.id = h.streak WHERE h.id = :id
    """
  )
  fun findByHabitId(id: UUID): Optional<Streak>
}
