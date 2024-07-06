package com.urosjarc.architect.lib.domain

import com.urosjarc.architect.lib.types.Id
import kotlinx.serialization.Serializable

@Serializable
public data class AMethod(
    val classId: Id<AClass>,
    val type: Type,
    val name: String,
    val visibility: AVisibility,
    val returnType: String,

    val id: Id<AMethod> = Id(),
) {
    public enum class Type {
        CONSTRUCTOR,
        METHOD,
    }
}
