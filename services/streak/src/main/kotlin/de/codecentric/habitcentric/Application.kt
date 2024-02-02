package de.codecentric.habitcentric

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class StreakApplication

fun main(args: Array<String>) {
  runApplication<StreakApplication>(*args)
}
