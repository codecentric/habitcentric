package de.codecentric.hc.track.habits;

import de.codecentric.hc.track.habits.HabitTracking.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitTrackingRepository extends JpaRepository<HabitTracking, Id> {

    List<HabitTracking> findByIdUserIdAndIdHabitId(String userId, Long habitId);

    void deleteByIdHabitId(Long habitId);
}
