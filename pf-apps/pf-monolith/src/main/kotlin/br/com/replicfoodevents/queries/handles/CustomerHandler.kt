package br.com.replicfoodevents.queries.handles

import br.com.replicfoodevents.customer.domain.api.CustomerCreatedEvent
import br.com.replicfoodevents.queries.models.CustomerEntity
import br.com.replicfoodevents.queries.repositories.CustomerRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.AllowReplay
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.SequenceNumber
import org.springframework.messaging.simp.SimpMessageSendingOperations
@ProcessingGroup("customer")
internal class CustomerHandler(
    private val repository: CustomerRepository,
    private val messagingTemplate: SimpMessageSendingOperations
) {
    @EventHandler
    @AllowReplay(true)
    fun handle(event: CustomerCreatedEvent, @SequenceNumber aggregateVersion: Long){
        repository.save(
            CustomerEntity(
                firstName = event.person.firstName,
                lastName = event.person.lastName,
                orderLimit = event.orderLimit.amount,
                aggregateVersion = aggregateVersion,
                id = event.aggregateIdentifier.identifier
            )
        )
        broadCastUpdates()
    }
    @ResetHandler
    fun onReset() = repository.deleteAll()

    private fun broadCastUpdates() = messagingTemplate.convertAndSend("/topic/customers.updates ", repository.findAll())
}