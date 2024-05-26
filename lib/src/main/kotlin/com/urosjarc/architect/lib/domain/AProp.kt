package com.urosjarc.architect.core.domain

import com.urosjarc.architect.DomainEntity
import com.urosjarc.architect.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AProp(
    val classId: Id<AClass>,
    val name: String,
    val type: String,
    val inlineType: String?,
    val visibility: String,
    val isMutable: Boolean,
    val isOptional: Boolean,
    val isAbstract: Boolean,
    val isConst: Boolean,
    val isFinal: Boolean,
    val isLateinit: Boolean,
    val isOpen: Boolean,
    val isSuspend: Boolean,
    val id: Id<AProp> = Id(),
)
