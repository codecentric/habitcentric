package de.codecentric.habitcentric.streak.database

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.relational.core.mapping.NamingStrategy

@Configuration
class DataJdbcConfiguration {

  @Bean
  fun databaseNamingSchema(): NamingStrategy = object : NamingStrategy {
    override fun getSchema(): String = "hc_streak"
  }
}
