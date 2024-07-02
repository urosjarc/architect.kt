package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import kotlin.String
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.Customer


@Serializable
public data class CustomerMod(
    val address: String,
    val city: String,
    val company: String,
    val firstName: String,
    val id: Id<Customer>,
    val lastName: String,
    val state: String,
)