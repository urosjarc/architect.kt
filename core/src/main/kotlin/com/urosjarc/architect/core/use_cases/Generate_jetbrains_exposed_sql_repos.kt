package com.urosjarc.architect.core.use_cases

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.lib.domain.AState

@UseCase
public class Generate_jetbrains_exposed_sql_repos {

    public sealed interface Result {
        public data class OK(val aState: AState) : Result

    }

    public fun now(aState: AState) {
        aState.repos.forEach {
            println(it)
        }
    }

}
