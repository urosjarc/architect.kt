package com.urosjarc.architect.example.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.example.types.Id
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

/**
 * Represents the GrandParent entity in the domain model
 */
@Serializable
@DomainEntity
public data class GrandParent(
    val name: String,
    val surname: String,
    val birth: Instant,
    val sex: Sex,
    val id: Id<GrandParent> = Id()
)
