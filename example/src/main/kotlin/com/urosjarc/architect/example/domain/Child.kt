package com.urosjarc.architect.example.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.example.types.Id
import kotlinx.datetime.Instant

@DomainEntity
public data class Child(
    val name: String,
    val surname: String,
    val birth: Instant,
    val parent: Id<Parent>,
    val sex: Sex,
    val id: Id<Child> = Id()
)
