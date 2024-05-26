package com.urosjarc.architect.core.domain

import com.urosjarc.architect.DomainEntity
import com.urosjarc.architect.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AClass(
    val id: Id<AClass> = Id(),
    val name: String,
    val path: String
)
