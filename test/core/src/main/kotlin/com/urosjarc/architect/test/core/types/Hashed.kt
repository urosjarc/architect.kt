package com.urosjarc.architect.test.core.types

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import kotlin.math.pow

internal val hasher = Argon2PasswordEncoder(16, 32, 1, 2.0.pow(14.0).toInt(), 3)

public fun String.hashed(): Hashed {
    val hash = hasher.encode(this)
    return Hashed(value = hash.encodeToByteArray())
}

@JvmInline
@Serializable
public value class Hashed(
    @Contextual public val value: ByteArray,
) {
    override fun toString(): String = this.value.decodeToString()

    public fun match(value: String): Boolean {
        return hasher.matches(value, this.value.decodeToString())
    }
}
