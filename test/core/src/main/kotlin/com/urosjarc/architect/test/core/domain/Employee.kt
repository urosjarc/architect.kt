package com.urosjarc.architect.test.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.test.core.types.Id
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class Employee(
    val id: Id<Employee> = Id(),
    val reportsToManager: Id<Employee>,
    val reportsTo: Id<Employee>,

    val lastName: String,
    val firstName: String,
    val title: String,
    val birthDate: Instant,
    val hireDate: Instant,
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: String,
    val phone: String,
    val fax: String,
    val email: String,
)
