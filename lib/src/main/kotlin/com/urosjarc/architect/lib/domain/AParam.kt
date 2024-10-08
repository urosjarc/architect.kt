package com.urosjarc.architect.lib.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.lib.types.Id
import kotlinx.serialization.Serializable

@Serializable
@DomainEntity
public data class AParam(
    val methodId: Id<AMethod>,
    val name: String,
    val type: String,
    val isOptional: Boolean,

    val id: Id<AParam> = Id(),
)
