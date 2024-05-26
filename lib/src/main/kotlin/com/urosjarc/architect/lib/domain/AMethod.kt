package com.urosjarc.architect.core.domain

import com.urosjarc.architect.core.types.Id
import kotlinx.serialization.Serializable

@Serializable
public data class AMethod(
    val classId: Id<AClass>,
    val type: Type,
    val name: String,
    val id: Id<AMethod> = Id(),
) {
    public enum class Type {
        CONSTRUCTOR,
        METHOD,
    }
}
