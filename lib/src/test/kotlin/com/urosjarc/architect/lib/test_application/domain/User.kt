package com.urosjarc.architect.lib.test_application.domain

import com.urosjarc.architect.annotations.DomainEntity
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
data class User(
    val email: String,
    val password: String,
    val type: Type,

    val id: Id<User> = Id(),
) {
    enum class Type { ADMIN, USER }
}
