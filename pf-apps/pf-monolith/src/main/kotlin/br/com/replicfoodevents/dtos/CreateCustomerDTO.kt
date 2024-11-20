package br.com.replicfoodevents.dtos

import java.math.BigDecimal

data class CreateCustomerDTO(
    val firstName: String,
    val lastName: String,
    val orderLimit: BigDecimal
)
