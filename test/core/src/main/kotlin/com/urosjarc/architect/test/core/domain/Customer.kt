package com.urosjarc.architect.test.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.annotations.Var
import com.urosjarc.architect.test.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class Customer(
    val id: Id<Customer> = Id(),
    val supportRepId: Id<Employee>,

    @Var val firstName: String = "",
    @Var val lastName: String = "",
    @Var val company: String,
    @Var val address: String,
    @Var val city: String,
    @Var val state: String,
    val country: String,
    val postalCode: String,
    val phone: String,
    val fax: String,
    val email: String,
)
