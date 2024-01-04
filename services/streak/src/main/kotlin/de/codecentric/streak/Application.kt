package de.codecentric.streak

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.relational.core.mapping.NamingStrategy

@SpringBootApplication
class StreakApplication

fun main(args: Array<String>) {
  runApplication<StreakApplication>(*args)
}
