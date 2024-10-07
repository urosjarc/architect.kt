package com.urosjarc.architect.lib.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.lib.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class ATypeParam(
    val propId: Id<AProp>?,
    val paramId: Id<AParam>?,
    val returnTypeId: Id<AReturnType>?,
    val name: String,
    val packagePath: String,

    val id: Id<ATypeParam> = Id(),
) {
    val import: String get() = "$packagePath.$name"
}
