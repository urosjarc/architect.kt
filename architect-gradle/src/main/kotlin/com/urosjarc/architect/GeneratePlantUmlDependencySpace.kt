package com.urosjarc.architect

import org.gradle.api.tasks.TaskAction

public open class GeneratePlantUmlDependencySpace : ArchitectTask(description = "Generate PlantUML class graph for dependency space.") {

    @TaskAction
    public fun doLast() {
        println(this::class.java.simpleName)
    }

}
