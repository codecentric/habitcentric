package de.codecentric.hc.habit.habits;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface HabitRepository extends JpaRepository<Habit, Long> {

  Optional<Habit> findByIdAndUserId(UUID id, String userId);

  List<Habit> findAllByUserIdOrderByNameAsc(String userId);

  @Transactional
  Long deleteByIdAndUserId(UUID id, String userId);
}
