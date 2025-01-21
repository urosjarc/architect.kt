package com.urosjarc.architect.example.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.example.types.Id
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Represents the Parent entity in the domain model.
 */
@Serializable
@DomainEntity
public data class Parent(
    val name: String,
    val surname: String,
    val birth: Instant,
    val parent: Id<GrandParent>,
    val sex: Sex,
    val id: Id<Parent> = Id()
)
