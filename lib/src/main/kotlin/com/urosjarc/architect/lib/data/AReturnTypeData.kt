package com.urosjarc.architect.lib.data

import com.urosjarc.architect.lib.domain.AReturnType
import com.urosjarc.architect.lib.domain.ATypeParam
import kotlinx.serialization.Serializable

@Serializable
public data class AReturnTypeData(
    val aReturnType: AReturnType,
    val aTypeParams: List<ATypeParam>,
)
