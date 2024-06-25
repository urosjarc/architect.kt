package com.urosjarc.architect.test.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.test.types.Id

@DomainEntity
data class User(
    val name: String,
    val surname: String,
    val age: Int,
    val height: Int,

    val id: Id<User> = Id()
)
