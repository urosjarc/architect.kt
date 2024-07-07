package com.urosjarc.architect.lib.data

import com.urosjarc.architect.lib.domain.AClass
import com.urosjarc.architect.lib.domain.AConstructor
import kotlinx.serialization.Serializable

@Serializable
public data class AClassData(
    val aClass: AClass,
    val aConstructor: AConstructor,
    val aProps: List<APropData>,
    val aMethods: List<AMethodData>,
)
