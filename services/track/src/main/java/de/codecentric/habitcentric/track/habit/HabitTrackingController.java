package de.codecentric.habitcentric.track.habit;

import de.codecentric.habitcentric.track.auth.UserId;
import de.codecentric.habitcentric.track.habit.validation.HabitId;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    var existingHabitTrackings = repository.findByIdUserIdAndIdHabitId(userId, habitId);

    untrackRemovedTrackingDates(dates, existingHabitTrackings);
    trackNewTrackingDates(userId, habitId, dates, existingHabitTrackings);

    return dates.stream().sorted().toList();
  }

  private void untrackRemovedTrackingDates(
      Set<LocalDate> dates, List<HabitTracking> existingHabitTrackings) {
    existingHabitTrackings.stream()
        .filter(ht -> !dates.contains(ht.getId().getTrackDate()))
        .forEach(
            ht -> {
              ht.untrack();
              repository.delete(ht);
            });
  }

  private void trackNewTrackingDates(String userId, Long habitId, Set<LocalDate> dates, List<HabitTracking> existingHabitTrackings) {
    var existingTrackDates =
            existingHabitTrackings.stream()
                                  .map(ht -> ht.getId().getTrackDate())
                                  .collect(Collectors.toSet());

    var newHabitTrackings =
            dates.stream()
                 .filter(date -> !existingTrackDates.contains(date))
                 .map(date -> new HabitTracking(userId, habitId, date))
                 .toList();

    repository.saveAll(newHabitTrackings);
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
    return extractDates(repository.findByIdUserIdAndIdHabitId(userId, habitId));
  }

  protected List<LocalDate> extractDates(List<HabitTracking> trackRecords) {
    return trackRecords.stream()
        .map(tracking -> tracking.getId().getTrackDate())
        .sorted()
        .collect(Collectors.toList());
  }
}
