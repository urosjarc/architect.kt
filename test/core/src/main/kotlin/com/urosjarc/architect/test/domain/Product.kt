package com.urosjarc.architect.test.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.test.types.Id

@DomainEntity
data class Product(
    val code: String,
    val price: Float,
    val buyer: Id<User>,

    val id: Id<Product> = Id()
)
