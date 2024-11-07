package br.com.replicfoodevents.customer.domain.api

import br.com.replicfoodevents.common.domain.api.AuditableAbstractEvent
import br.com.replicfoodevents.common.domain.api.model.AuditEntry
import br.com.replicfoodevents.common.domain.api.model.Money
import br.com.replicfoodevents.common.domain.api.model.PersonName
import br.com.replicfoodevents.customer.domain.api.model.CustomerId
import br.com.replicfoodevents.customer.domain.api.model.CustomerOrderId

abstract class  CustomerEvent(open val aggregateIdentifier: CustomerId, override val auditEntry: AuditEntry)
    : AuditableAbstractEvent(auditEntry)

abstract class  CustomerOrderEvent(open val aggregateIdentifier: CustomerOrderId, override val auditEntry: AuditEntry)
    : AuditableAbstractEvent(auditEntry)



data class CustomerCreatedEvent(
    val person: PersonName,
    val orderLimit: Money,
    override val aggregateIdentifier: CustomerId,
    override val auditEntry: AuditEntry
): CustomerEvent(aggregateIdentifier, auditEntry)

data class CustomerOrderCreatedEvent(
    val orderTotal: Money,
    val customerOrderId: CustomerOrderId,
    override val aggregateIdentifier: CustomerId,
    override val auditEntry: AuditEntry
): CustomerEvent(aggregateIdentifier, auditEntry)

data class CustomerOrderDeliveredEvent(
    override val aggregateIdentifier: CustomerOrderId,
    override val auditEntry: AuditEntry
) : CustomerOrderEvent(aggregateIdentifier, auditEntry)

data class CustomerOrderRejectedEvent(
    override val aggregateIdentifier: CustomerOrderId,
    override val auditEntry: AuditEntry
) : CustomerOrderEvent(aggregateIdentifier, auditEntry)
