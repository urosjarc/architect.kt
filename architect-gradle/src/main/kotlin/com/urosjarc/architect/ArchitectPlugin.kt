package com.urosjarc.architect

import org.gradle.api.Plugin
import org.gradle.api.Project

public class ArchitectPlugin : Plugin<Project> {

    internal val className: String = ArchitectPlugin::class.simpleName.toString()

    override fun apply(project: Project) {
        project.tasks.register("generate domain models", GenerateDomainModelsTasks::class.java)
        project.tasks.register("generate jetbrains exposed repositories", GenerateJetbrainsExposedRepositories::class.java)
        project.tasks.register("generate plant uml dependency space", GeneratePlantUmlDependencySpace::class.java)
        project.tasks.register("generate plant uml domain space", GeneratePlantUmlDomainSpace::class.java)
        project.tasks.register("generate raw dependency object", GenerateRawDependencyObject::class.java)
    }
}
