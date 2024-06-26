package com.urosjarc.architect.test.core.use_cases.base

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.lib.domain.AState
import com.urosjarc.architect.test.core.repos.UserRepo

@UseCase
class Generate_jetbrains_exposed_sql_repos(
    private val userRepo: UserRepo
) {

    sealed interface Result {
        data class OK(val aState: AState) : Result

    }

    fun now(aState: AState) {
        aState.repos.forEach {
            println(it)
        }
    }

}
