package de.codecentric.hc.habit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name", name = "unique_habit_name"))
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "habit_id_generator")
    @SequenceGenerator(name = "habit_id_generator", sequenceName = "habit_seq")
    private Long id;

    private String name;

    @Embedded
    private Schedule schedule;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Schedule {

        private Integer repetitions;

        @Enumerated(EnumType.STRING)
        private Frequency frequency;

        enum Frequency {DAILY, WEEKLY, MONTHLY, YEARLY}
    }

    public static Habit from(ModificationRequest modificationRequest) {
        return builder()
                .name(modificationRequest.getName())
                .schedule(modificationRequest.getSchedule())
                .build();
    }

    /**
     * Used to create or update {@link Habit}s.
     * This class does not have an ID. The habit ID cannot be updated and cannot be determined before creating a habit.
     */
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class ModificationRequest {

        private String name;

        private Schedule schedule;
    }
}
