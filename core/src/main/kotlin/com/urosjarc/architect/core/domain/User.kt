package com.urosjarc.architect.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.core.types.Encrypted
import com.urosjarc.architect.core.types.Hashed
import com.urosjarc.architect.lib.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class User(
    val email: Encrypted,
    val password: Hashed,
    val type: Type,
    val id: Id<User> = Id(),
) {
    public enum class Type {
        ADMIN, USER
    }
}
