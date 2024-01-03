package de.codecentric.habitcentric.track.habit;

import de.codecentric.habitcentric.track.auth.UserId;
import de.codecentric.habitcentric.track.habit.validation.HabitId;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Validated
public class HabitTrackingController {

  private final HabitTrackingRepository repository;

  public HabitTrackingController(HabitTrackingRepository repository) {
    this.repository = repository;
  }

  @Transactional
  @PutMapping("/track/habits/{habitId}")
  @ResponseBody
  public Collection<LocalDate> putHabitTrackingRecordsWithJwt(
      @UserId String userId,
      @PathVariable @HabitId Long habitId,
      @RequestBody Set<LocalDate> dates) {
    return putHabitTrackingRecords(userId, habitId, dates);
  }

  @Transactional
  @PutMapping("/track/users/{userId}/habits/{habitId}")
  @ResponseBody
  public Collection<LocalDate> putHabitTrackingRecords(
      @PathVariable @UserId String userId,
      @PathVariable @HabitId Long habitId,
      @RequestBody Set<LocalDate> dates) {
    var existingHabitTrackings =
        repository
            .findByIdUserIdAndIdHabitId(userId, habitId)
            .orElse(HabitTracking.from(userId, habitId));

    existingHabitTrackings.track(dates);

    repository.save(existingHabitTrackings);

    return existingHabitTrackings.getSortedTrackingDates();
  }

  @GetMapping("/track/habits/{habitId}")
  @ResponseBody
  public Iterable<LocalDate> getHabitTrackingRecordsWithJwt(
      @UserId String userId, @PathVariable @HabitId Long habitId) {
    return getHabitTrackingRecords(userId, habitId);
  }

  @GetMapping("/track/users/{userId}/habits/{habitId}")
  @ResponseBody
  public Iterable<LocalDate> getHabitTrackingRecords(
      @PathVariable @UserId String userId, @PathVariable @HabitId Long habitId) {
    return repository
        .findByIdUserIdAndIdHabitId(userId, habitId)
        .map(HabitTracking::getSortedTrackingDates)
        .orElse(Collections.emptyList());
  }
}
