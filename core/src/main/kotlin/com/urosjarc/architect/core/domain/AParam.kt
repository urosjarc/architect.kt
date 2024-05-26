package com.urosjarc.architect.core.domain

import com.urosjarc.architect.DomainEntity
import com.urosjarc.architect.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AParam(
    val id: Id<AParam> = Id(),
    val classId: Id<AClass>,
    val name: String,
    val type: String,
    val kind: String,
    val isOptional: Boolean,
)
