package com.urosjarc.architect

import org.gradle.api.tasks.TaskAction

public open class GenerateJetbrainsExposedRepositories : ArchitectTask(description = "Generate Jetbrains Exposed repositories for applied project.") {
    @TaskAction
    public fun doLast() {
        println(this::class.java.simpleName)
    }
}
