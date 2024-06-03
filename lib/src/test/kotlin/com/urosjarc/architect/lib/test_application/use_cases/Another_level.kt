package com.urosjarc.architect.lib.test_application.use_cases

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.lib.test_application.repos.UserRepo

@UseCase
class Another_level(
    private val userRepo: UserRepo,
    private val seedRepos: Seed_repos
) {

    fun now() {
        TODO()
    }

}
