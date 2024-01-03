package de.codecentric.streak

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StreakApplication

fun main(args: Array<String>) {
  runApplication<StreakApplication>(*args)
}
