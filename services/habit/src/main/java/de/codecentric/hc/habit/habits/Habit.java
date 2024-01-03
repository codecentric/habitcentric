package de.codecentric.hc.habit.habits;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
@Table(
    uniqueConstraints =
        @UniqueConstraint(columnNames = "name", name = Habit.CONSTRAINT_NAME_UNIQUE_NAME))
public class Habit extends AbstractAggregateRoot<Habit> {

  public static final String CONSTRAINT_NAME_UNIQUE_NAME = "unique_habit_name";

  @Id private UUID id;

  @NotBlank
  @Size(max = 64)
  private String name;

  @Embedded @NotNull @Valid private Schedule schedule;

  @NotBlank
  @Size(min = 5, max = 64)
  private String userId;

  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Setter
  @Getter
  @EqualsAndHashCode
  @ToString
  public static class Schedule {

    @NotNull @Positive private Integer repetitions;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Frequency frequency;

    public enum Frequency {
      DAILY,
      WEEKLY,
      MONTHLY,
      YEARLY
    }
  }

  public static Habit from(ModificationRequest modificationRequest, String userId) {
    Habit habit =
        builder()
            .id(UUID.randomUUID())
            .name(modificationRequest.getName())
            .schedule(modificationRequest.getSchedule())
            .userId(userId)
            .build();
    habit.registerEvent(
        new Habit.HabitCreated(
            habit.id, habit.name, habit.schedule.frequency, habit.schedule.repetitions));

    return habit;
  }

  /**
   * Used to create or update {@link Habit}s. This class does not have an ID. The habit ID cannot be
   * updated and cannot be determined before creating a habit.
   */
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  @Setter
  @Getter
  @EqualsAndHashCode
  @ToString
  public static class ModificationRequest {

    @NotBlank
    @Size(max = 64)
    private String name;

    @NotNull @Valid private Schedule schedule;
  }

  public record HabitCreated(
      UUID habitId, String name, Schedule.Frequency frequency, Integer repetitions) {
    public String getId() {
      return habitId.toString();
    }
  }
}
