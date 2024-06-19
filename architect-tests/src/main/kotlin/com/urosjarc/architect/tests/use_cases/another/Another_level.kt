package com.urosjarc.architect.lib.test_application.use_cases.another

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.lib.test_application.repos.UserRepo
import com.urosjarc.architect.lib.test_application.use_cases.base.Seed_repos

@UseCase
class Another_level(
    private val userRepo: UserRepo,
    private val seed_repos: Seed_repos
) {

    fun now() {
        TODO()
    }

}
