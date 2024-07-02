package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import kotlin.String
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.Employee


@Serializable
public data class CustomerNew(
    val address: String,
    val city: String,
    val company: String,
    val country: String,
    val email: String,
    val fax: String,
    val phone: String,
    val postalCode: String,
    val state: String,
    val supportRepId: Id<Employee>,
)