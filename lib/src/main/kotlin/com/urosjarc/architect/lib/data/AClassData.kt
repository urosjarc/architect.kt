package com.urosjarc.architect.lib.data

import com.urosjarc.architect.lib.domain.AClass
import com.urosjarc.architect.lib.domain.AProp
import kotlinx.serialization.Serializable

@Serializable
public data class AClassData(
    val aClass: AClass,
    val aProps: List<AProp>,
    val aMethods: List<AMethodData>
)
