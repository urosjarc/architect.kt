package com.urosjarc.architect.lib.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.lib.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AEnum(
    val name: String,
    val packagePath: String,
    val module: String?,
    val visibility: AVisibility,
    val type: String?,
    val docs: String?,

    val id: Id<AEnum> = Id(),
) {
    val import: String get() = "$packagePath.$name"
}
