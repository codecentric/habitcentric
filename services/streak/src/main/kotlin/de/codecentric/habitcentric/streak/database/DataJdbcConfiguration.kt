package de.codecentric.habitcentric.streak.database

import de.codecentric.habitcentric.streak.AggregateRoot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.NamingStrategy
import org.springframework.data.relational.core.mapping.event.AfterSaveCallback
import java.util.UUID

@Configuration
class DataJdbcConfiguration {

  @Bean
  fun databaseNamingSchema(): NamingStrategy = object : NamingStrategy {
    override fun getSchema(): String = "hc_streak"
  }

  @Bean
  fun afterSaveCallback(): AfterSaveCallback<AggregateRoot<UUID>> =
    AfterSaveCallback { aggregate: AggregateRoot<UUID> -> aggregate.saved() }
}
