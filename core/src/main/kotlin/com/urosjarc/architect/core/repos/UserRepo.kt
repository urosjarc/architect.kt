package com.urosjarc.architect.core.repos

import com.urosjarc.architect.annotations.Repository
import com.urosjarc.architect.core.domain.User

@Repository
public interface UserRepo : Repo<User> {
    public fun find(email: String): List<User>
}
