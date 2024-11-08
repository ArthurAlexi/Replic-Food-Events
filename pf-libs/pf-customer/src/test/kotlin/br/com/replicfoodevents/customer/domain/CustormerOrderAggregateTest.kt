package br.com.replicfoodevents.customer.domain

import br.com.replicfoodevents.common.domain.api.model.AuditEntry
import br.com.replicfoodevents.common.domain.api.model.Money
import br.com.replicfoodevents.customer.domain.api.CustomerOrderCreatedEvent
import br.com.replicfoodevents.customer.domain.api.CustomerOrderDeliveredEvent
import br.com.replicfoodevents.customer.domain.api.MarkCustomerOrderAsDeliveredCommand
import br.com.replicfoodevents.customer.domain.api.model.CustomerId
import br.com.replicfoodevents.customer.domain.api.model.CustomerOrderId
import org.axonframework.messaging.interceptors.BeanValidationInterceptor
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Before
import org.junit.Test

import java.math.BigDecimal
import java.util.Calendar

class CustormerOrderAggregateTest {
    private lateinit var fixture: FixtureConfiguration<CustomerOrder>
    private val who = "lgertel"
    private val auditEntry: AuditEntry = AuditEntry(who, Calendar.getInstance().time)
    private val orderId: CustomerOrderId = CustomerOrderId("orderId")
    private val customerId: CustomerId = CustomerId("customerId")
    private val orderTotal: Money = Money(BigDecimal.valueOf(100))

    @Before
    fun setup() {
        fixture = AggregateTestFixture(CustomerOrder::class.java)
        fixture.registerCommandDispatchInterceptor(BeanValidationInterceptor())
    }

    @Test
    fun markOrderAsDeliveredTest() {
        val customerOrderCreatedEvent = CustomerOrderCreatedEvent(
            orderTotal =  orderTotal,
            aggregateIdentifier =  customerId,
            customerOrderId =  orderId,
            auditEntry =    auditEntry
        )

        val markCustomerOrderAsDeliveredCommand = MarkCustomerOrderAsDeliveredCommand(orderId, auditEntry)
        val customerOrderDeliveredEvent = CustomerOrderDeliveredEvent(orderId, auditEntry)

        fixture.given(customerOrderCreatedEvent)
            .`when`(markCustomerOrderAsDeliveredCommand)
            .expectEvents(customerOrderDeliveredEvent)
    }

}