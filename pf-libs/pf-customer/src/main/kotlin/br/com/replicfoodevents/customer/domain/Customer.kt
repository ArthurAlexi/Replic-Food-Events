package br.com.replicfoodevents.customer.domain

import br.com.replicfoodevents.common.domain.api.model.Money
import br.com.replicfoodevents.common.domain.api.model.PersonName
import br.com.replicfoodevents.customer.domain.api.CreateCustomerCommand
import br.com.replicfoodevents.customer.domain.api.CreateCustomerOrderCommand
import br.com.replicfoodevents.customer.domain.api.CustomerCreatedEvent
import br.com.replicfoodevents.customer.domain.api.model.CustomerId
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate(snapshotTriggerDefinition = "customerSnapshotTriggerDefinition")
internal class Customer {
    @AggregateIdentifier
    private lateinit var id: CustomerId
    private lateinit var person: PersonName
    private lateinit var orderList: Money

    constructor()
    @CommandHandler
    constructor(command: CreateCustomerCommand ) {
        AggregateLifecycle.apply(CustomerCreatedEvent(
            person = command.person,
            orderLimit = command.orderLimit,
            aggregateIdentifier = command.targetAggregateIdentifier,
            auditEntry = command.auditEntry))
    }
    @EventSourcingHandler
    fun on(event: CustomerCreatedEvent) {
        this.id = event.aggregateIdentifier
        this.person = event.person
        this.orderList = event.orderLimit
    }
    @CommandHandler
    fun han(command: CreateCustomerOrderCommand){
        if( !command.orderTotal.isGreaterThanOrEqual(orderList)){
            AggregateLifecycle.createNew(CustomerOrder::class.java) { CustomerOrder(command) }
        }else {
            throw UnsupportedOperationException("Customer limit is reached")
        }
    }

    override fun toString(): String = ToStringBuilder.reflectionToString(this)
    override fun equals(other: Any?): Boolean = EqualsBuilder.reflectionEquals(this, other)
    override fun hashCode(): Int = HashCodeBuilder.reflectionHashCode(this)
}