package com.urosjarc.architect.lib.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.lib.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AEnumValue(
    val id: Id<AEnumValue> = Id(),
    val name: String,
    val value: String?,
    val docs: String?
)
