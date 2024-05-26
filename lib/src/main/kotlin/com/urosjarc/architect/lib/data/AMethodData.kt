package com.urosjarc.architect.core.data

import com.urosjarc.architect.DomainEntity
import com.urosjarc.architect.core.domain.AMethod
import com.urosjarc.architect.core.domain.AParam
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AMethodData(
    val aMethod: AMethod,
    val aParams: List<AParam>
)
