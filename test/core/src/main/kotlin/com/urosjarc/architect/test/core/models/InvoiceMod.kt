package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.Invoice


@Serializable
public data class InvoiceMod(
    val id: Id<Invoice>,
)