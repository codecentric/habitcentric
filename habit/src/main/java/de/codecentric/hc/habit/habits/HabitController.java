package de.codecentric.hc.habit.habits;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import de.codecentric.hc.habit.habits.Habit.ModificationRequest;
import de.codecentric.hc.habit.validation.UserId;
import java.net.URI;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@Slf4j
@Validated
public class HabitController {
  private final HabitRepository repository;

  public HabitController(HabitRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/habits")
  @ResponseBody
  public Iterable<Habit> getHabits(@UserId String userId) {
    return repository.findAllByUserIdOrderByNameAsc(userId);
  }

  @GetMapping("/habits/{id}")
  @ResponseBody
  public Habit getHabit(@PathVariable Long id, @UserId String userId) {
    return repository
        .findByIdAndUserId(id, userId)
        .orElseThrow(
            () ->
                new ResponseStatusException(
                    NOT_FOUND, String.format("Habit '%s' could not be found.", id)));
  }

  @PostMapping("/habits")
  public ResponseEntity createHabit(
      @RequestBody @Valid ModificationRequest modificationRequest, @UserId String userId) {

    try {
      Habit habit = repository.save(Habit.from(modificationRequest, userId));

      URI location =
          ServletUriComponentsBuilder.fromCurrentRequest()
              .path("/{id}")
              .buildAndExpand(habit.getId())
              .toUri();

      return ResponseEntity.created(location).build();
    } catch (DataAccessException e) {
      throw handleDatabaseException(e);
    }
  }

  @DeleteMapping("/habits/{id}")
  public ResponseEntity deleteHabit(@PathVariable Long id, @UserId String userId) {
    Long deletedRecords = repository.deleteByIdAndUserId(id, userId);
    if (deletedRecords < 1) {
      throw new ResponseStatusException(
          NOT_FOUND, String.format("Habit '%s' could not be found.", id));
    }
    return ResponseEntity.ok().build();
  }

  private ResponseStatusException handleDatabaseException(DataAccessException e) {

    if (e instanceof DataIntegrityViolationException
        && e.getCause() instanceof ConstraintViolationException) {
      ConstraintViolationException constraintViolation =
          (ConstraintViolationException) e.getCause();
      if ("unique_habit_name".equals(constraintViolation.getConstraintName())) {
        throw new ResponseStatusException(BAD_REQUEST, "Please choose a unique habit name.");
      }
    }

    String defaultErrorMessage = "An unexpected database exception occurred.";
    log.error(defaultErrorMessage, e);
    throw new ResponseStatusException(INTERNAL_SERVER_ERROR, defaultErrorMessage);
  }
}
