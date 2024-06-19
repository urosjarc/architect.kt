package com.urosjarc.architect.lib.test_application.domain

import com.urosjarc.architect.annotations.Identifier
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Identifier
@Serializable
@JvmInline
value class Id<T>(@Contextual val value: UUID = UUID.randomUUID()) {
    override fun toString(): String = this.value.toString()
}
