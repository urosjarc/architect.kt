package com.urosjarc.e2e

import com.urosjarc.architect.core.DomainEntity
import java.util.*


@JvmInline
value class Id<T>(val value: UUID = UUID.randomUUID())

@DomainEntity
data class Parent(
    val id: Id<Parent>,
    val name: String
)

@DomainEntity
data class Child(
    val id: Id<Child>,
    val name: String,
    val parent: Id<Parent>
)
