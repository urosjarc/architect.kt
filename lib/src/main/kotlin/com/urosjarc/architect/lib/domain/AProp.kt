package com.urosjarc.architect.lib.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.annotations.Identifier
import com.urosjarc.architect.annotations.Mod
import com.urosjarc.architect.annotations.New
import com.urosjarc.architect.lib.extend.ext_name
import com.urosjarc.architect.lib.types.Id
import kotlinx.serialization.Serializable

@Serializable
@DomainEntity
public data class AProp(
    val classId: Id<AClass>,
    val name: String,
    val packagePath: String,
    val type: String,
    val inlineType: String?,
    val annotations: List<String>,
    val visibility: AVisibility,
    val isMutable: Boolean,
    val isNullable: Boolean,
    val isOptional: Boolean,
    val isAbstract: Boolean,
    val isConst: Boolean,
    val isFinal: Boolean,
    val isLateinit: Boolean,
    val isOpen: Boolean,
    var isIdentifier: Boolean,
    val docs: String?,

    val id: Id<AProp> = Id(),
) {

    val import: String get() = "$packagePath.$type"
    val isMod: Boolean get() = this.isIdentifier || this.annotations.contains(Mod::class.ext_name)
    val isNew: Boolean get() = this.annotations.contains(New::class.ext_name)
}
