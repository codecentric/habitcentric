package de.codecentric.habitcentric.track.habit;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.modulith.events.Externalized;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Getter
@Setter
@ToString
@Table
public class HabitTracking extends AbstractAggregateRoot<HabitTracking> {

  @EmbeddedId @Valid private Id id;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "tracked_dates",
      joinColumns = {@JoinColumn(name = "habit_id"), @JoinColumn(name = "user_id")})
  @Column(name = "tracking_date")
  private Set<LocalDate> trackings;

  public void track(Set<LocalDate> dates) {
    var untrackedDates = calculateUntrackedDates(dates);
    var trackedDates = calculateTrackedDates(dates);

    untrackedDates.forEach(this::registerUntrackedEvent);
    trackedDates.forEach(this::registerTrackedEvent);

    trackings = new HashSet<>(dates);
  }

  private Set<LocalDate> calculateUntrackedDates(Set<LocalDate> dates) {
    Set<LocalDate> copy = new HashSet<>(trackings);
    copy.removeAll(dates);
    return copy;
  }

  private Set<LocalDate> calculateTrackedDates(Set<LocalDate> dates) {
    Set<LocalDate> copy = new HashSet<>(dates);
    copy.removeAll(trackings);
    return copy;
  }

  private void registerTrackedEvent(LocalDate date) {
    registerEvent(new DateTracked(id.userId, id.habitId, date));
  }

  private void registerUntrackedEvent(LocalDate date) {
    registerEvent(new DateUntracked(id.userId, id.habitId, date));
  }

  public List<LocalDate> getSortedTrackingDates() {
    return trackings.stream().sorted().toList();
  }

  public static HabitTracking from(String userId, UUID habitId) {
    return new HabitTracking(new Id(userId, habitId), new HashSet<>());
  }

  public static HabitTracking from(String userId, UUID habitId, Collection<LocalDate> trackings) {
    return new HabitTracking(new Id(userId, habitId), new HashSet<>(trackings));
  }

  @Embeddable
  @AllArgsConstructor
  @NoArgsConstructor
  @Getter
  @Setter
  @EqualsAndHashCode
  @ToString
  public static class Id implements Serializable {
    @NotBlank
    @Size(max = 64)
    private String userId;

    @NotNull private UUID habitId;
  }

  @Externalized("habit-tracking-events::#{#this.getId()}")
  public record DateTracked(String userId, UUID habitId, LocalDate trackDate) {
    public String getId() {
      return userId + "-" + habitId;
    }
  }

  @Externalized("habit-tracking-events::#{#this.getId()}")
  public record DateUntracked(String userId, UUID habitId, LocalDate trackDate) {
    public String getId() {
      return userId + "-" + habitId;
    }
  }
}
