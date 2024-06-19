package com.urosjarc.architect

import com.urosjarc.architect.lib.Architect
import org.gradle.api.tasks.TaskAction

public open class GenerateDomainModelsTasks : ArchitectTask(description = "Generate domain models for applied project.") {
    @TaskAction
    public fun doLast() {
        val aState = Architect.getState("com.urosjarc.architect")
        logger.warn("======================================")
        aState.domainEntities.forEach {
            logger.warn(it.toString())
        }
    }
}
