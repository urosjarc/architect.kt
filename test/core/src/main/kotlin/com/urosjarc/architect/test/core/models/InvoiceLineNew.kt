package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.Customer
import kotlin.Int
import com.urosjarc.architect.test.core.domain.Track
import kotlin.Float


@Serializable
public data class InvoiceLineNew(
    val customerId: Id<Customer>,
    val quantity: Int,
    val trackId: Id<Track>,
    val unitPrice: Float,
)