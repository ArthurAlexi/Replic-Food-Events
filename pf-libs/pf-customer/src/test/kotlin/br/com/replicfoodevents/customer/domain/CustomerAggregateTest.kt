package br.com.replicfoodevents.customer.domain

import br.com.replicfoodevents.common.domain.api.model.AuditEntry
import br.com.replicfoodevents.common.domain.api.model.Money
import br.com.replicfoodevents.common.domain.api.model.PersonName
import br.com.replicfoodevents.customer.domain.api.CreateCustomerCommand
import br.com.replicfoodevents.customer.domain.api.CreateCustomerOrderCommand
import br.com.replicfoodevents.customer.domain.api.CustomerCreatedEvent
import br.com.replicfoodevents.customer.domain.api.CustomerOrderCreatedEvent
import br.com.replicfoodevents.customer.domain.api.model.CustomerId
import org.axonframework.messaging.interceptors.BeanValidationInterceptor
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal
import java.util.Calendar

class CustomerAggregateTest {
    private lateinit var fixture: FixtureConfiguration<Customer>
    private val who = "lgertel"
    private val auditEntry: AuditEntry = AuditEntry(who, Calendar.getInstance().time)
    private val auditEntry2: AuditEntry = AuditEntry(who + "2", Calendar.getInstance().time)
    private val orderLimit = Money(BigDecimal.valueOf(1000000))

    @Before
    fun setUp() {
        fixture = AggregateTestFixture(Customer::class.java)
        fixture.registerCommandDispatchInterceptor(BeanValidationInterceptor())
    }

    @Test
    fun createCustomerTest() {
        val name = PersonName("user", "test")
        val createCustomerCommand = CreateCustomerCommand(
            person =  name,
            orderLimit =  orderLimit,
            auditEntry = auditEntry)
        val customerCreatedEvent = CustomerCreatedEvent(name, orderLimit, createCustomerCommand.targetAggregateIdentifier, auditEntry)

        fixture.given()
            .`when`(createCustomerCommand)
            .expectEvents(customerCreatedEvent)
    }

    @Test
    fun createCustomerOrderTest() {
        val name = PersonName("test", "user")
        val createCustomerOrderCommand = CreateCustomerOrderCommand(CustomerId("customerId"), orderLimit, auditEntry)
        val customerCreatedEvent = CustomerCreatedEvent(name, orderLimit.add(Money(BigDecimal.ONE)), createCustomerOrderCommand.targetAggregateIdentifier, auditEntry)
        val customerOrderCreatedEvent = CustomerOrderCreatedEvent(
            orderLimit,
            createCustomerOrderCommand.customerOrderId,
            CustomerId("customerId"),
            auditEntry)

        fixture.given(customerCreatedEvent)
            .`when`(createCustomerOrderCommand)
            .expectEvents(customerOrderCreatedEvent)
    }

    @Test
    fun createCustomerOrderFailOrderLimitTest() {
        val name = PersonName("test", "user")
        val createCustomerOrderCommand = CreateCustomerOrderCommand(CustomerId("customerId"), orderLimit, auditEntry)
        val customerCreatedEvent = CustomerCreatedEvent(
            person =  name,
            orderLimit = orderLimit,
            aggregateIdentifier =  createCustomerOrderCommand.targetAggregateIdentifier,
            auditEntry = auditEntry)
//        val customerOrderCreatedEvent = CustomerOrderCreatedEvent(orderLimit, createCustomerOrderCommand.customerOrderId, CustomerId("customerId"), auditEntry)

        fixture.given(customerCreatedEvent)
            .`when`(createCustomerOrderCommand)
            .expectException(UnsupportedOperationException::class.java)
    }
}