package com.urosjarc.architect.lib.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.lib.types.Id
import kotlinx.serialization.Serializable

@Serializable
@DomainEntity
public data class AConstructor(
    val classId: Id<AClass>,
    val id: Id<AMethod> = Id(),
)
