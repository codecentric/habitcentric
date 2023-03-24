package de.codecentric.hc.habit.habits;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Table(
    uniqueConstraints =
        @UniqueConstraint(columnNames = "name", name = Habit.CONSTRAINT_NAME_UNIQUE_NAME))
public class Habit {

  public static final String CONSTRAINT_NAME_UNIQUE_NAME = "unique_habit_name";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habit_id_generator")
  @SequenceGenerator(name = "habit_id_generator", sequenceName = "habit_seq")
  private Long id;

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
    return builder()
        .name(modificationRequest.getName())
        .schedule(modificationRequest.getSchedule())
        .userId(userId)
        .build();
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
}
