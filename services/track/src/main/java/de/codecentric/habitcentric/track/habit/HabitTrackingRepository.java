package de.codecentric.habitcentric.track.habit;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitTrackingRepository extends JpaRepository<HabitTracking, HabitTracking.Id> {
  Optional<HabitTracking> findByIdUserIdAndIdHabitId(String userId, UUID habitId);
}
