package com.urosjarc.architect

import org.gradle.api.DefaultTask

public open class ArchitectTask(private val description: String) : DefaultTask() {
    override fun getGroup(): String? = "architect"
    override fun getDescription(): String? = this.description
}
