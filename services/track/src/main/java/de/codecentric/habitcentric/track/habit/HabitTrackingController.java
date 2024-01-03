package de.codecentric.habitcentric.track.habit;

import de.codecentric.habitcentric.track.auth.UserId;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
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
  @PutMapping("/track/habits/{habitIdString}")
  @ResponseBody
  public Collection<LocalDate> putHabitTrackingRecordsWithJwt(
      @UserId String userId,
      @org.hibernate.validator.constraints.UUID @PathVariable String habitIdString,
      @RequestBody Set<LocalDate> dates) {
    return putHabitTrackingRecords(userId, habitIdString, dates);
  }

  @Transactional
  @PutMapping("/track/users/{userId}/habits/{habitIdString}")
  @ResponseBody
  public Collection<LocalDate> putHabitTrackingRecords(
      @PathVariable @UserId String userId,
      @org.hibernate.validator.constraints.UUID @PathVariable String habitIdString,
      @RequestBody Set<LocalDate> dates) {
    var habitId = UUID.fromString(habitIdString);
    var existingHabitTrackings =
        repository
            .findByIdUserIdAndIdHabitId(userId, habitId)
            .orElse(HabitTracking.from(userId, habitId));

    existingHabitTrackings.track(dates);

    repository.save(existingHabitTrackings);

    return existingHabitTrackings.getSortedTrackingDates();
  }

  @GetMapping("/track/habits/{habitIdString}")
  @ResponseBody
  public Iterable<LocalDate> getHabitTrackingRecordsWithJwt(
      @UserId String userId,
      @org.hibernate.validator.constraints.UUID @PathVariable String habitIdString) {
    return getHabitTrackingRecords(userId, habitIdString);
  }

  @GetMapping("/track/users/{userId}/habits/{habitIdString}")
  @ResponseBody
  public Iterable<LocalDate> getHabitTrackingRecords(
      @PathVariable @UserId String userId,
      @org.hibernate.validator.constraints.UUID @PathVariable String habitIdString) {
    return repository
        .findByIdUserIdAndIdHabitId(userId, UUID.fromString(habitIdString))
        .map(HabitTracking::getSortedTrackingDates)
        .orElse(Collections.emptyList());
  }
}
