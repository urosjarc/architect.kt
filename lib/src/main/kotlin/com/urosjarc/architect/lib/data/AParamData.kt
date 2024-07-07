package com.urosjarc.architect.lib.data

import com.urosjarc.architect.lib.domain.AParam
import com.urosjarc.architect.lib.domain.ATypeParam
import kotlinx.serialization.Serializable

@Serializable
public data class AParamData(
    val aParam: AParam,
    val aTypeParams: List<ATypeParam>,
)
