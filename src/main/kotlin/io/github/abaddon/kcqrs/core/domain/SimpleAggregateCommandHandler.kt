package io.github.abaddon.kcqrs.core.domain

import io.github.abaddon.kcqrs.core.IAggregate
import io.github.abaddon.kcqrs.core.domain.messages.commands.ICommand
import io.github.abaddon.kcqrs.core.persistence.IAggregateRepository
import java.util.*

class SimpleAggregateCommandHandler<TAggregate: IAggregate> (
    override val repository: IAggregateRepository<TAggregate>,
): IAggregateCommandHandler<TAggregate>{
    override suspend fun handle(command: ICommand<TAggregate>, updateHeaders: () -> Map<String,String>) {
        val aggregate=repository.getById(command.aggregateID)
        val newAggregate=command.execute(aggregate)
        repository.save(newAggregate, UUID.randomUUID(), updateHeaders)
    }

    override suspend fun handle(command: ICommand<TAggregate>) {
        handle(command) { mapOf<String, String>() }
    }
}