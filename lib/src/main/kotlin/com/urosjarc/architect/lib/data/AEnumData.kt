package com.urosjarc.architect.lib.data

import com.urosjarc.architect.lib.domain.AEnum
import com.urosjarc.architect.lib.domain.AEnumValue
import kotlinx.serialization.Serializable

@Serializable
public data class AEnumData(
    val aEnum: AEnum,
    val aEnumValues: List<AEnumValue>
)
