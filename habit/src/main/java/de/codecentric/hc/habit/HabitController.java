package de.codecentric.hc.habit;

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
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class HabitController {

    private final HabitRepository repository;

    public HabitController(HabitRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/habits")
    @ResponseBody
    public Iterable<Habit> getHabits() {
        return repository.findAll();
    }

    @PostMapping("/habits")
    public ResponseEntity createHabit(@RequestBody HabitModificationRequest modificationRequest) {

        if (StringUtils.isEmpty(StringUtils.trimWhitespace(modificationRequest.getName()))) {
            throw new ResponseStatusException(BAD_REQUEST, "Please provide a valid habit name.");
        }

        Habit habit = repository.save(habitFrom(modificationRequest));

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(habit.getId())
                .toUri();

        return ResponseEntity.created(location).build();
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
}
