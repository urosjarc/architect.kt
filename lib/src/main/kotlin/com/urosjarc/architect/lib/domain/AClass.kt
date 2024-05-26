package com.urosjarc.architect.core.domain

import com.urosjarc.architect.DomainEntity
import com.urosjarc.architect.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AClass(
    val name: String,
    val path: String,
    val module: String,
    val id: Id<AClass> = Id(),
)
