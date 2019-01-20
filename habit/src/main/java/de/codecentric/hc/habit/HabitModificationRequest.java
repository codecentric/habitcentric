package de.codecentric.hc.habit;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Used to create or update {@link Habit}s.
 * This class does not have an ID. The habit ID cannot be updated and is not determined when creating new habits.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class HabitModificationRequest {

    private String name;
}
