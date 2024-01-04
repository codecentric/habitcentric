package de.codecentric.streak.domain

import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface StreakRepository : CrudRepository<Streak, UUID> {
}
