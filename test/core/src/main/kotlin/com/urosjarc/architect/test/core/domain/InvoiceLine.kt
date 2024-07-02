package com.urosjarc.architect.test.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.test.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class InvoiceLine(
    val id: Id<InvoiceLine> = Id(),
    val customerId: Id<Customer>,
    val trackId: Id<Track>,

    val unitPrice: Float,
    val quantity: Int,
)
