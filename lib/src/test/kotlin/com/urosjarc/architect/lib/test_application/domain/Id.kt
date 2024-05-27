package com.urosjarc.architect.lib.test_application.domain

import com.urosjarc.architect.annotations.Identificator
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Identificator
@Serializable
@JvmInline
value class Id<T>(@Contextual public val value: UUID = UUID.randomUUID()) {
    override fun toString(): String = this.value.toString()
}
