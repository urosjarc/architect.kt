package com.urosjarc.architect.test.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.annotations.Mod
import com.urosjarc.architect.annotations.New
import com.urosjarc.architect.test.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
data class User(
    @Mod @New val email: String,
    @New @Mod val password: String,
    @New @Mod val type: Type,

    val id: Id<User> = Id(),
) {
    enum class Type { ADMIN, USER }
}
