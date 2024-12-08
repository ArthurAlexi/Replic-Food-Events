package br.com.replicfoodevents.restaurant.domain.api

import br.com.replicfoodevents.common.domain.api.AuditableAbstractCommand
import br.com.replicfoodevents.common.domain.api.model.AuditEntry
import br.com.replicfoodevents.restaurant.domain.api.model.RestaurantId
import br.com.replicfoodevents.restaurant.domain.api.model.RestaurantMenu
import br.com.replicfoodevents.restaurant.domain.api.model.RestaurantOrderDetails
import br.com.replicfoodevents.restaurant.domain.api.model.RestaurantOrderId
import org.axonframework.modelling.command.TargetAggregateIdentifier
import javax.validation.Valid

abstract class RestaurantCommand(open val targetAggregateIdentifier: RestaurantId, override val auditEntry: AuditEntry)
    : AuditableAbstractCommand(auditEntry)

abstract class RestaurantOrderCommand(open val targetAggregateIdentifier: RestaurantOrderId, override val auditEntry: AuditEntry)
    : AuditableAbstractCommand(auditEntry)

data class CreateRestaurantCommand(
    val name: String,
    @field:Valid val menu: RestaurantMenu,
    @TargetAggregateIdentifier override val targetAggregateIdentifier: RestaurantId,
    override val auditEntry: AuditEntry
) : RestaurantCommand(targetAggregateIdentifier, auditEntry){
    constructor(name: String, menu: RestaurantMenu,auditEntry: AuditEntry) :
            this(
                name = name,
                menu = menu,
                targetAggregateIdentifier = RestaurantId(),
                auditEntry = auditEntry
            )
}

data class CreateRestaurantOrderCommand(
    @field:Valid val orderDetails: RestaurantOrderDetails,
    val restaurantOrderId: RestaurantOrderId,
    @TargetAggregateIdentifier override val targetAggregateIdentifier: RestaurantId,
    override val auditEntry: AuditEntry
) : RestaurantCommand(targetAggregateIdentifier, auditEntry) {
    constructor(targetAggregateIdentifier: RestaurantId, orderDetails: RestaurantOrderDetails, auditEntry: AuditEntry) :
            this(
                auditEntry = auditEntry,
                restaurantOrderId = RestaurantOrderId(),
                targetAggregateIdentifier = targetAggregateIdentifier,
                orderDetails = orderDetails
            )
}

data class MarkCustomerOrderAsPrepared(
    @TargetAggregateIdentifier override val targetAggregateIdentifier: RestaurantOrderId,
    override val auditEntry: AuditEntry
) : RestaurantOrderCommand(targetAggregateIdentifier, auditEntry)