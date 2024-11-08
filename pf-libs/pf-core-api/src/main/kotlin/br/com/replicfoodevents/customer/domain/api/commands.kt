package br.com.replicfoodevents.customer.domain.api

import br.com.replicfoodevents.common.domain.api.AuditableAbstractCommand
import br.com.replicfoodevents.common.domain.api.model.AuditEntry
import br.com.replicfoodevents.common.domain.api.model.Money
import br.com.replicfoodevents.common.domain.api.model.PersonName
import br.com.replicfoodevents.customer.domain.api.model.CustomerId
import br.com.replicfoodevents.customer.domain.api.model.CustomerOrderId
import org.axonframework.modelling.command.TargetAggregateIdentifier
import javax.validation.Valid

abstract class CustomerCommand(open val targetAggregateIdentifier: CustomerId, override val auditEntry: AuditEntry)
    : AuditableAbstractCommand(auditEntry)

abstract class CustomerOrderCommand(open val targetAggregateIdentifier: CustomerOrderId, override val auditEntry: AuditEntry)
    : AuditableAbstractCommand(auditEntry)

data class CreateCustomerCommand(
    @field:Valid val person : PersonName,
    val orderLimit: Money,
    @TargetAggregateIdentifier override val targetAggregateIdentifier: CustomerId,
    override val auditEntry: AuditEntry
): CustomerCommand(targetAggregateIdentifier, auditEntry) {
    constructor(person: PersonName, orderLimit: Money, auditEntry: AuditEntry) : this(
        orderLimit = orderLimit,
        auditEntry = auditEntry,
        targetAggregateIdentifier =CustomerId(),
        person = person)
}
data class CreateCustomerOrderCommand(
    val customerOrderId: CustomerOrderId,
    @field:Valid val orderTotal: Money,
    @TargetAggregateIdentifier override val targetAggregateIdentifier: CustomerId,
    override val auditEntry: AuditEntry
) : CustomerCommand(targetAggregateIdentifier, auditEntry) {
    constructor(targetAggregateIdentifier: CustomerId, orderTotal: Money, auditEntry: AuditEntry) :
            this(CustomerOrderId(), orderTotal, targetAggregateIdentifier,  auditEntry)
}

data class MarkCustomerOrderAsDeliveredCommand(
    @TargetAggregateIdentifier override val targetAggregateIdentifier: CustomerOrderId,
    override val auditEntry: AuditEntry
) : CustomerOrderCommand(targetAggregateIdentifier, auditEntry)