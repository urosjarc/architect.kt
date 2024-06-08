package com.urosjarc.architect.lib.test_application.use_cases.base

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.lib.test_application.repos.UserRepo
import com.urosjarc.architect.lib.test_application.use_cases.base.Generate_jetbrains_exposed_sql_repos

@UseCase
class Seed_repos(
    private val userRepo: UserRepo,
    private val generateJetbrainsExposedSqlRepos: Generate_jetbrains_exposed_sql_repos
) {

    fun now() {
        TODO()
    }

}
