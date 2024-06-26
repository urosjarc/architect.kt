package com.urosjarc.architect.test.core.use_cases

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.test.core.use_cases.another.Another_level
import com.urosjarc.architect.test.core.use_cases.base.Generate_jetbrains_exposed_sql_repos

@UseCase
class LowestLevel(
    private val generate_jetbrains_exposed_sql_repos: Generate_jetbrains_exposed_sql_repos,
    private val another_level: Another_level
) {

    fun now() {
        TODO()
    }

}
