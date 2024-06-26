package com.urosjarc.architect.test.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.annotations.Mod
import com.urosjarc.architect.annotations.New
import com.urosjarc.architect.test.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
data class Product(
    @Mod val userId: Id<User>,
    @Mod @New val name: String,
    @Mod val id: Id<Product> = Id(),
)
