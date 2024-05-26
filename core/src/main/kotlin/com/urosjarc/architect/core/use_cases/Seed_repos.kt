package com.urosjarc.architect.core.use_cases

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.core.domain.User
import com.urosjarc.architect.core.repos.UserRepo
import com.urosjarc.architect.core.types.encrypted
import com.urosjarc.architect.core.types.hashed

@UseCase
public class Seed_repos(
    private val userRepo: UserRepo
) {

    public fun now() {
        this.userRepo.insert(
            user = User(email = "email".encrypted(), password = "password".hashed(), type = User.Type.USER)
        )
    }

}
