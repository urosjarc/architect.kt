package com.urosjarc.architect.lib.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.lib.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AClass(
    val name: String,
    val packagePath: String,
    val module: String?,
    val docs: String?,

    val isInterface: Boolean,
    val isAbstract: Boolean,
    val isData: Boolean,
    val isFinal: Boolean,
    val isInner: Boolean,
    val isOpen: Boolean,
    val isSealed: Boolean,
    val isValue: Boolean,

    val visibility: AVisibility,

    val id: Id<AClass> = Id(),
) {
    val import: String get() = "$packagePath.$name"
}
