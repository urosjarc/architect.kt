package com.urosjarc.architect

import org.gradle.api.tasks.TaskAction

public open class GeneratePlantUmlDomainSpace : ArchitectTask(description = "Generate PlantUML class graph for domain space.") {

    @TaskAction
    public fun doLast() {
        println(this::class.java.simpleName)
    }
}
