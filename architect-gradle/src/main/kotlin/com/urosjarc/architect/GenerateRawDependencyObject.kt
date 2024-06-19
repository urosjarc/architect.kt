package com.urosjarc.architect

import org.gradle.api.tasks.TaskAction

public open class GenerateRawDependencyObject : ArchitectTask(description = "Generate raw dependency code for App object.") {

    @TaskAction
    public fun doLast() {
        println(this::class.java.simpleName)
    }

}
