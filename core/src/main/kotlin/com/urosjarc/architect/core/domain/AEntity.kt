package com.urosjarc.architect.core.domain

import com.urosjarc.architect.DomainEntity
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AEntity(
    val aClass: AClass,
    val aParams: List<AParam>,
    val aProps: List<AProp>
)
