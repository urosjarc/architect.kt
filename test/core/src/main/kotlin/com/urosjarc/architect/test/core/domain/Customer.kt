package com.urosjarc.architect.test.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.test.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class Customer(
    val id: Id<Customer> = Id(),
    val supportRepId: Id<Employee>,

    var firstName: String = "",
    var lastName: String = "",
    var company: String,
    var address: String,
    var city: String,
    var state: String,
    val country: String,
    val postalCode: String,
    val phone: String,
    val fax: String,
    val email: String,
)
