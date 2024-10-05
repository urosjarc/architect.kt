package com.urosjarc.architect.lib.types

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@JvmInline
public value class Id<T>(@Contextual public val value: UUID = UUID.randomUUID()) {
    override fun toString(): String = this.value.toString()
}
@Serializable
@JvmInline
public value class IdDouble<T, Q>(@Contextual public val value: UUID = UUID.randomUUID()) {
    override fun toString(): String = this.value.toString()
}
