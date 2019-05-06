package de.codecentric.hc.track.habits;

import de.codecentric.hc.track.habits.validation.HabitId;
import de.codecentric.hc.track.habits.validation.UserId;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
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
  public List<LocalDate> putHabitTrackingRecordsWithJwt(
      @UserId String userId,
      @PathVariable @HabitId Long habitId,
      @RequestBody Set<LocalDate> dates) {
    return putHabitTrackingRecords(userId, habitId, dates);
  }

  @Transactional
  @PutMapping("/track/users/{userId}/habits/{habitId}")
  @ResponseBody
  public List<LocalDate> putHabitTrackingRecords(
      @PathVariable @UserId String userId,
      @PathVariable @HabitId Long habitId,
      @RequestBody Set<LocalDate> dates) {
    repository.deleteByIdHabitId(habitId);
    Set<HabitTracking> trackRecords =
        dates.stream()
            .map(date -> new HabitTracking(userId, habitId, date))
            .collect(Collectors.toSet());
    return extractDates(repository.saveAll(trackRecords));
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
