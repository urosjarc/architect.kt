package com.urosjarc.architect.test.core.use_cases.another

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.test.core.use_cases.base.Seed_repos

@UseCase
class Another_level(
    private val userRepo: UserRepo,
    private val seed_repos: Seed_repos
) {

    fun now() {
        TODO()
    }

}
