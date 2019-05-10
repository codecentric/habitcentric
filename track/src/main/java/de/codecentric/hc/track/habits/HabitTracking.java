package de.codecentric.hc.track.habits;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
@Table
public class HabitTracking {

  @EmbeddedId @Valid private Id id;

  public HabitTracking(String userId, Long habitId, LocalDate trackDate) {
    this.id = new HabitTracking.Id(userId, habitId, trackDate);
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

    @NotNull @Positive private Long habitId;

    @NotNull private LocalDate trackDate;
  }
}
