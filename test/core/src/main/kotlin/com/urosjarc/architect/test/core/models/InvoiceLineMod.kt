package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.InvoiceLine


@Serializable
public data class InvoiceLineMod(
    val id: Id<InvoiceLine>,
)