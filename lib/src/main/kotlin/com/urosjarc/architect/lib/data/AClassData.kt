package com.urosjarc.architect.core.data

import com.urosjarc.architect.DomainEntity
import com.urosjarc.architect.core.domain.AClass
import com.urosjarc.architect.core.domain.AProp
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AClassData(
    val aClass: AClass,
    val aProps: List<AProp>,
    val aMethods: List<AMethodData>
)
