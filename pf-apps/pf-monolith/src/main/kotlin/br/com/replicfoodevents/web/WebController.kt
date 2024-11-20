package br.com.replicfoodevents.web

import br.com.replicfoodevents.common.domain.api.model.AuditEntry
import br.com.replicfoodevents.common.domain.api.model.Money
import br.com.replicfoodevents.common.domain.api.model.PersonName
import br.com.replicfoodevents.customer.domain.api.CreateCustomerCommand
import br.com.replicfoodevents.dtos.CreateCustomerDTO
import br.com.replicfoodevents.queries.models.CustomerEntity
import br.com.replicfoodevents.queries.repositories.CustomerRepository
import org.axonframework.commandhandling.callbacks.LoggingCallback
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SubscribeMapping
import org.springframework.stereotype.Controller
import java.math.BigDecimal
import java.util.Calendar

@Controller
class WebController(
    private val commandGateway: CommandGateway,
    private val customerRepository: CustomerRepository
) {

    private val auditEntry: AuditEntry
        get() = AuditEntry("TEST", Calendar.getInstance().time)

    @MessageMapping("/customers/createcommand")
    fun createCustomer(request: CreateCustomerDTO) = commandGateway.send(
        CreateCustomerCommand(
            PersonName( firstName = request.firstName, lastName = request.lastName ),
            Money(request.orderLimit),
            auditEntry
        ),
        LoggingCallback.INSTANCE
    )
    @SubscribeMapping("/customers")
    fun allCustomers(): Iterable<CustomerEntity> = customerRepository.findAll()
    @SubscribeMapping("/customers/{id}")
    fun getCustomers(@DestinationVariable id: String) = customerRepository.findById(id)
}