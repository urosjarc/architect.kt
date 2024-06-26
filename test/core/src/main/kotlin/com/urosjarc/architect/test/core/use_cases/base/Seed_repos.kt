package com.urosjarc.architect.test.core.use_cases.base

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.test.core.repos.UserRepo

@UseCase
class Seed_repos(
    private val userRepo: UserRepo,
    private val generate_jetbrains_exposed_sql_repos: Generate_jetbrains_exposed_sql_repos
) {

    fun now() {
        TODO()
    }

}
