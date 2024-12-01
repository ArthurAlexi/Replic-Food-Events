package br.com.replicfoodevents.common.domain.api.model

import java.math.BigDecimal
import java.util.Date

data class AuditEntry(val who: String, val `when`: Date)

data class PersonName(val firstName: String, val lastName: String)

data class Money(val amount: BigDecimal){

    constructor() : this(BigDecimal(0))
    fun add(delta: Money): Money = Money(amount.add(delta.amount))

    fun multiply(x: Int) : Money = Money(amount.multiply(BigDecimal(x)))

    fun isGreaterThanOrEqual(other: Money) : Boolean = amount >= other.amount
}