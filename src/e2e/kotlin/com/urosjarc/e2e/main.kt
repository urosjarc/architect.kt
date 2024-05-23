package com.urosjarc.e2e

import com.urosjarc.architect.Architect


fun main() {
    val arch = Architect(packageName = "com.urosjarc.e2e")
    println("\nUse cases: ")
    arch.useCases.forEach {
        println(it)
    }
    println("\nServices: ")
    arch.services.forEach {
        println(it)
    }
    println("\nDomain entities: ")
    arch.domainEntities.forEach {
        println(it)
    }
    println("\nRepos: ")
    arch.repos.forEach {
        println(it)
    }
}
