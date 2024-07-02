package com.urosjarc.architect.test.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.test.core.types.Id
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class Invoice(
    val id: Id<Invoice> = Id(),
    val customerId: Id<Customer>,

    val invoiceDate: Instant,
    val billingAddress: String,
    val billingCity: String,
    val billingState: String,
    val billingCountry: String,
    val billingPostalCode: String,
    val total: Float,
)
