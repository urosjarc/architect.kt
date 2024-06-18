package com.urosjarc.architect

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class GenerateJetbrainsExposedRepositories : DefaultTask() {
    @TaskAction
    fun doLast(){
        println(this::class.java.simpleName)
    }
}
