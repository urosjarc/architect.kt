package com.urosjarc.architect.lib.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.lib.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AClass(
    val name: String,
    val path: String,
    val module: String?,

    val id: Id<AClass> = Id(),
)
