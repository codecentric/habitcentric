package de.codecentric.habitcentric.track.habit;

import de.codecentric.habitcentric.track.habit.HabitTracking.Id;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitTrackingRepository extends JpaRepository<HabitTracking, Id> {

  List<HabitTracking> findByIdUserIdAndIdHabitId(String userId, Long habitId);

  void deleteByIdHabitId(Long habitId);
}
