package de.codecentric.hc.habit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
