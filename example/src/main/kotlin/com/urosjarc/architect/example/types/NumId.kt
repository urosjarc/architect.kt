package com.urosjarc.architect.example.types

import com.urosjarc.architect.annotations.Identifier
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
@JvmInline
@Identifier
public value class NumId<T>(@Contextual public val value: Int) {
    override fun toString(): String = this.value.toString()
}
