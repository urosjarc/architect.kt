package com.urosjarc.architect.lib.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.lib.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class ATypeParam(
    val propId: Id<AProp>,
    val name: String,
    val path: String,

    val id: Id<ATypeParam> = Id(),
)
