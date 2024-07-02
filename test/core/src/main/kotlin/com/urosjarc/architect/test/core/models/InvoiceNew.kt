package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import kotlin.String
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.Customer
import kotlinx.datetime.Instant
import kotlin.Float


@Serializable
public data class InvoiceNew(
    val billingAddress: String,
    val billingCity: String,
    val billingCountry: String,
    val billingPostalCode: String,
    val billingState: String,
    val customerId: Id<Customer>,
    val invoiceDate: Instant,
    val total: Float,
)