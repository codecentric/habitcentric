package de.codecentric.hc.habit.habits;

import static de.codecentric.hc.habit.habits.Habit.Schedule.Frequency.YEARLY;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.codecentric.hc.habit.habits.Habit.ModificationRequest;
import de.codecentric.hc.habit.habits.Habit.Schedule;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class HabitControllerTest {

  private HabitController controller;
  private HabitRepository repository;
  private static final String userId = "dummy";
  private static final UUID habitId = UUID.randomUUID();
  private static final Habit DEFAULT_HABIT =
      Habit.builder()
          .id(habitId)
          .name("ABC")
          .userId(userId)
          .schedule(new Schedule(2, YEARLY))
          .build();
  private static final ModificationRequest DEFAULT_MODIFICATION =
      new ModificationRequest(DEFAULT_HABIT.getName(), DEFAULT_HABIT.getSchedule());

  @BeforeEach
  public void beforeEach() {
    repository = mock(HabitRepository.class);
    controller = new HabitController(repository);
  }

  @Test
  public void getHabitShouldReturnHabits() {
    List<Habit> expected = asList(DEFAULT_HABIT);
    when(repository.findAllByUserIdOrderByNameAsc(eq(userId))).thenReturn(expected);
    assertThat(controller.getHabits(userId)).isEqualTo(expected);
  }

  @Test
  public void getHabitShouldReturnHabitById() {
    Habit expected = DEFAULT_HABIT;
    when(repository.findByIdAndUserId(eq(habitId), eq(userId))).thenReturn(Optional.of(expected));
    assertThat(controller.getHabit(habitId.toString(), userId)).isEqualTo(expected);
  }

  @Test
  public void getHabitShouldThrowAnExceptionWhenHabitNotFound() {
    when(repository.findByIdAndUserId(eq(habitId), eq(userId))).thenReturn(Optional.empty());
    assertThatExceptionOfType(ResponseStatusException.class)
        .isThrownBy(() -> controller.getHabit(habitId.toString(), userId))
        .withMessageMatching("404 NOT_FOUND \"Habit '(.*)' could not be found.\"");
  }

  @Test
  public void createHabitShouldCreateHabit() {
    HttpServletRequest request = new MockHttpServletRequest("POST", "/habits");
    ServletRequestAttributes requestAttributes = new ServletRequestAttributes(request);
    RequestContextHolder.setRequestAttributes(requestAttributes);
    URI expectedLocation =
        ServletUriComponentsBuilder.fromRequest(request)
            .path("/{id}")
            .buildAndExpand(habitId)
            .toUri();
    when(repository.save(any())).thenReturn(DEFAULT_HABIT);
    assertThat(controller.createHabit(DEFAULT_MODIFICATION, userId))
        .isEqualTo(ResponseEntity.created(expectedLocation).build());
  }

  @Test
  public void createHabitShouldHandleDataAccessException() {
    DataAccessException exception = new DataRetrievalFailureException("UNEXPECTED");
    when(repository.save(any())).thenThrow(exception);
    assertThatExceptionOfType(ResponseStatusException.class)
        .isThrownBy(() -> controller.createHabit(DEFAULT_MODIFICATION, userId))
        .withMessage("500 INTERNAL_SERVER_ERROR \"An unexpected database exception occurred.\"");
  }

  @Test
  public void createHabitShouldHandleDataIntegrityViolationException() {
    DataAccessException exception =
        new DataIntegrityViolationException("ABC", new Exception("other cause"));
    when(repository.save(any())).thenThrow(exception);
    assertThatExceptionOfType(ResponseStatusException.class)
        .isThrownBy(() -> controller.createHabit(DEFAULT_MODIFICATION, userId))
        .withMessage("500 INTERNAL_SERVER_ERROR \"An unexpected database exception occurred.\"");
  }

  @Test
  public void createHabitShouldHandleConstraintViolationException() {
    DataAccessException exception =
        new DataIntegrityViolationException(
            "ABC",
            new ConstraintViolationException("DEF", mock(SQLException.class), "other_constraint"));
    when(repository.save(any())).thenThrow(exception);
    assertThatExceptionOfType(ResponseStatusException.class)
        .isThrownBy(() -> controller.createHabit(DEFAULT_MODIFICATION, userId))
        .withMessage("500 INTERNAL_SERVER_ERROR \"An unexpected database exception occurred.\"");
  }

  @Test
  public void createHabitShouldHandleUniqueHabitNameConstraintViolationException() {
    DataAccessException exception =
        new DataIntegrityViolationException(
            "ABC",
            new ConstraintViolationException(
                "DEF", mock(SQLException.class), Habit.CONSTRAINT_NAME_UNIQUE_NAME));
    when(repository.save(any())).thenThrow(exception);
    assertThatExceptionOfType(ResponseStatusException.class)
        .isThrownBy(() -> controller.createHabit(DEFAULT_MODIFICATION, userId))
        .withMessage("400 BAD_REQUEST \"Please choose a unique habit name.\"");
  }

  @Test
  public void deleteHabitShouldDeleteHabit() {
    when(repository.findByIdAndUserId(eq(habitId), eq(userId)))
        .thenReturn(Optional.of(DEFAULT_HABIT));
    assertThat(controller.deleteHabit(habitId.toString(), userId))
        .isEqualTo(ResponseEntity.ok().build());
  }

  @Test
  public void deleteHabitShouldThrowExceptionWhenHabitNotFound() {
    when(repository.deleteByIdAndUserId(eq(habitId), eq(userId))).thenReturn(0l);
    assertThatExceptionOfType(ResponseStatusException.class)
        .isThrownBy(() -> controller.deleteHabit(habitId.toString(), userId))
        .withMessageMatching("404 NOT_FOUND \"Habit '(.*)' could not be found.\"");
  }
}
