package com.urosjarc.architect.lib.impl

import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.domain.AState
import java.io.File

public class RawDependencyInjectionGenerator(
    private val appFile: File,
) : Generator {

    private data class AClassDataNode(
        val aClassData: AClassData,
        val dependencies: MutableList<AClassDataNode> = mutableListOf(),
        var active: Boolean = true
    ) {
        override fun toString(): String {
            return aClassData.aClass.packagePath
        }
    }

    private val allUseCases = mutableMapOf<String, AClassDataNode>()
    private var allOtherDeps = mapOf<String, AClassDataNode>()

    override fun generate(aState: AState) {

        /** GET REPOS AND SERVICES */
        allOtherDeps = (aState.repos + aState.services).map { it.aClass.packagePath to AClassDataNode(it) }.toMap()

        /** FILL USECASES */
        aState.useCases.forEach { allUseCases[it.aClass.packagePath] = AClassDataNode(aClassData = it) }

        /** CREATE USE CASE GRAPH TREE */
        allUseCases.forEach { packagePath, useCase: AClassDataNode ->
            useCase.aClassData.aProps.forEach {
                val dep = allUseCases[it.aProp.type]
                if (dep != null) {
                    useCase.dependencies.add(dep)
                }
            }
        }

        repeat(allUseCases.size) {

            val useCases = allUseCases.filter { it.value.active && it.value.dependencies.filter { it.active }.isEmpty() }

            if(useCases.isEmpty()) return@repeat


            useCases.forEach { packagePath, aClassDataNode ->
                println(packagePath)
                aClassDataNode.active = false
            }

            println("Next iteration")
        }

        //TODO: Build App dependency object.
    }

}
