package com.urosjarc.architect.lib.test_application.domain

import com.urosjarc.architect.annotations.DomainEntity
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
data class Product(
    val userId: UId<User>,
    val name: String,

    val id: UId<Product> = UId(),
)
