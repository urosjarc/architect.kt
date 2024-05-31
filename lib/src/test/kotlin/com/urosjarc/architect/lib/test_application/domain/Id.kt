package com.urosjarc.architect.lib.test_application.domain

import com.urosjarc.architect.annotations.Identificator
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Identificator
@Serializable
@JvmInline
value class UId<T>(@Contextual val value: UUID = UUID.randomUUID()) {
    override fun toString(): String = this.value.toString()
}

@Identificator
@Serializable
@JvmInline
value class Id<T>(val value: Int) {
    override fun toString(): String = this.value.toString()
}
