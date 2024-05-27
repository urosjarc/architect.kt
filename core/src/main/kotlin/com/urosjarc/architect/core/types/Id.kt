package com.urosjarc.architect.core.types

import com.urosjarc.architect.annotations.Identificator
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Identificator
@Serializable
@JvmInline
public value class Id<T>(@Contextual public val value: UUID = UUID.randomUUID()) {
    override fun toString(): String = this.value.toString()
}
