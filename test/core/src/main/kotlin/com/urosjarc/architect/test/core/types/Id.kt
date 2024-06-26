package com.urosjarc.architect.test.core.types

import com.urosjarc.architect.annotations.Identifier
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@JvmInline
@Identifier
public value class Id<T>(@Contextual public val value: UUID = UUID.randomUUID()) {
    override fun toString(): String = this.value.toString()
}
