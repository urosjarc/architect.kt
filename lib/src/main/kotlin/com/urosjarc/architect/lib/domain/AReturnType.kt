package com.urosjarc.architect.lib.domain

import com.urosjarc.architect.lib.types.Id
import kotlinx.serialization.Serializable

@Serializable
public data class AReturnType(
    val type: String,
    val methodId: Id<AMethod>,
    val id: Id<AReturnType> = Id()
)
