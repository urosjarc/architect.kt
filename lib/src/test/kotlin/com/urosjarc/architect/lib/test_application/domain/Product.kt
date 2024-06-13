package com.urosjarc.architect.lib.test_application.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.annotations.Mod
import com.urosjarc.architect.annotations.New
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
data class Product(
    @Mod val userId: Id<User>,
    @Mod @New val name: String,
    @Mod val id: Id<Product> = Id(),
)
