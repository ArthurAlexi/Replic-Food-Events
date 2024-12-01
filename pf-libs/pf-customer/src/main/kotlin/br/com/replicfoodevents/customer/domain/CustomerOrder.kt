package br.com.replicfoodevents.customer.domain

import br.com.replicfoodevents.common.domain.api.model.Money
import br.com.replicfoodevents.customer.domain.api.CreateCustomerOrderCommand
import br.com.replicfoodevents.customer.domain.api.CustomerOrderCreatedEvent
import br.com.replicfoodevents.customer.domain.api.CustomerOrderDeliveredEvent
import br.com.replicfoodevents.customer.domain.api.MarkCustomerOrderAsDeliveredCommand
import br.com.replicfoodevents.customer.domain.api.model.CustomerId
import br.com.replicfoodevents.customer.domain.api.model.CustomerOrderId
import br.com.replicfoodevents.customer.domain.api.model.CustomerOrderState
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.apache.commons.lang3.builder.ToStringBuilder
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate(snapshotTriggerDefinition = "customerSnapshotTriggerDefinition")
internal class CustomerOrder {

    @AggregateIdentifier
    private lateinit var id: CustomerOrderId
    private lateinit var customerId: CustomerId
    private lateinit var orderTotal: Money
    private lateinit var state: CustomerOrderState

    constructor()

    constructor(command: CreateCustomerOrderCommand){
        AggregateLifecycle.apply(CustomerOrderCreatedEvent(
            aggregateIdentifier = command.targetAggregateIdentifier,
            customerOrderId = command.customerOrderId,
            orderTotal = command.orderTotal,
            auditEntry = command.auditEntry
        ))
    }
    @EventSourcingHandler
    fun on(event: CustomerOrderCreatedEvent){
        this.id = event.customerOrderId
        this.customerId = event.aggregateIdentifier
        this.orderTotal = event.orderTotal
        this.state = CustomerOrderState.CREATED
    }
    @CommandHandler
    fun han(command: MarkCustomerOrderAsDeliveredCommand) {
        when (state) {
            CustomerOrderState.CREATED -> {
                AggregateLifecycle.apply(
                    CustomerOrderDeliveredEvent(
                        command.targetAggregateIdentifier,
                        command.auditEntry
                    )
                )
            }
            else -> throw IllegalStateException("The current state is not CREATED.")
        }
    }

    @EventSourcingHandler
    fun on(event: CustomerOrderDeliveredEvent){
        this.state = CustomerOrderState.DELIVERED
    }

    override fun toString(): String = ToStringBuilder.reflectionToString(this)
    override fun equals(other: Any?): Boolean = EqualsBuilder.reflectionEquals(this, other)
    override fun hashCode(): Int = HashCodeBuilder.reflectionHashCode(this)
}