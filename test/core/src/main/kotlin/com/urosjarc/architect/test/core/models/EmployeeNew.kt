package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import kotlin.String
import kotlinx.datetime.Instant
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.Employee


@Serializable
public data class EmployeeNew(
    val address: String,
    val birthDate: Instant,
    val city: String,
    val country: String,
    val email: String,
    val fax: String,
    val firstName: String,
    val hireDate: Instant,
    val lastName: String,
    val phone: String,
    val postalCode: String,
    val reportsTo: Id<Employee>,
    val reportsToManager: Id<Employee>,
    val state: String,
    val title: String,
)