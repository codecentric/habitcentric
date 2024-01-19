package de.codecentric.habitcentric.streak

import org.springframework.data.domain.Persistable

interface AggregateRoot<ID> : Persistable<ID> {

  fun saved(): AggregateRoot<ID>
}
