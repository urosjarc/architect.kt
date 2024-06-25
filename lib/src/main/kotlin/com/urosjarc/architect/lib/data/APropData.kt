package com.urosjarc.architect.lib.data

import com.urosjarc.architect.lib.domain.AProp
import com.urosjarc.architect.lib.domain.ATypeParam
import kotlinx.serialization.Serializable

@Serializable
public data class APropData(
    val aProp: AProp,
    val aTypeParams: List<ATypeParam>,
)
