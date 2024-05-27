package com.urosjarc.architect.lib.test_application.use_cases

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.lib.test_application.repos.UserRepo

@UseCase
class Seed_repos(
    private val userRepo: UserRepo
) {

    fun now() {
        TODO()
    }

}
