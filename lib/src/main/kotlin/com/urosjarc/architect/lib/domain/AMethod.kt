package com.urosjarc.architect.lib.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.lib.types.Id
import kotlinx.serialization.Serializable

@Serializable
@DomainEntity
public data class AMethod(
    val classId: Id<AClass>,
    val name: String,
    val visibility: AVisibility,
    val id: Id<AMethod> = Id(),
)
