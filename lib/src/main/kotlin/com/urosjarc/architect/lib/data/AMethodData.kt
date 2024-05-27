package com.urosjarc.architect.lib.data

import com.urosjarc.architect.lib.domain.AMethod
import com.urosjarc.architect.lib.domain.AParam
import kotlinx.serialization.Serializable

@Serializable
public data class AMethodData(
    val aMethod: AMethod,
    val aParams: List<AParam>
)
