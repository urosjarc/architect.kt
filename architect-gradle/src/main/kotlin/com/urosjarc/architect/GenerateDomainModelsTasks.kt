package com.urosjarc.architect

import com.urosjarc.architect.lib.Architect
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class GenerateDomainModelsTasks : DefaultTask() {
    @TaskAction
    fun doLast() {
        val aState = Architect.getState("com.urosjarc.architect")
        logger.warn("======================================")
        aState.domainEntities.forEach {
            logger.warn(it.toString())
        }
    }
}
