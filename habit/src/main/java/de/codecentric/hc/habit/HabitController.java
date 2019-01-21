package de.codecentric.hc.habit;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@Slf4j
public class HabitController {

    private final HabitRepository repository;

    public HabitController(HabitRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/habits")
    @ResponseBody
    public Iterable<Habit> getHabits() {
        return repository.findAllByOrderByNameAsc();
    }

    @PostMapping("/habits")
    public ResponseEntity createHabit(@RequestBody HabitModificationRequest modificationRequest) {

        if (StringUtils.isEmpty(StringUtils.trimWhitespace(modificationRequest.getName()))) {
            throw new ResponseStatusException(BAD_REQUEST, "Please provide a valid habit name.");
        }

        try {
            Habit habit = repository.save(habitFrom(modificationRequest));

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(habit.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        } catch (DataAccessException e) {
            throw handleDatabaseException(e);
        }
    }

    @DeleteMapping("/habits/{id}")
    public ResponseEntity deleteHabit(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(NOT_FOUND, String.format("Habit '%s' could not be found.", id), e);
        }
    }

    private Habit habitFrom(HabitModificationRequest modificationRequest) {
        return Habit.builder().name(modificationRequest.getName()).build();
    }

    private ResponseStatusException handleDatabaseException(DataAccessException e) {

        if (e instanceof DataIntegrityViolationException && e.getCause() instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolation = (ConstraintViolationException) e.getCause();
            if ("unique_habit_name".equals(constraintViolation.getConstraintName())) {
                throw new ResponseStatusException(BAD_REQUEST, "Please choose a unique habit name.");
            }
        }

        String defaultErrorMessage = "An unexpected database exception occurred.";
        log.error(defaultErrorMessage, e);
        throw new ResponseStatusException(INTERNAL_SERVER_ERROR, defaultErrorMessage);
    }
}
