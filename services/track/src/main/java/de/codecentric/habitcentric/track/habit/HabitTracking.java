package de.codecentric.habitcentric.track.habit;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
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

  public HabitTracking(String userId, Long habitId, LocalDate trackDate) {
    this.id = new HabitTracking.Id(userId, habitId, trackDate);

    registerEvent(new DateTracked(userId, habitId, trackDate));
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

  @Externalized("habit-tracking-events::#{#this.getId()}")
  public record DateTracked(String userId, Long habitId, LocalDate trackDate) {
    public String getId() {
      return userId + "-" + habitId;
    }
  }
}
