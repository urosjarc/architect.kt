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
        val dependencies: MutableList<AClassDataNode> = mutableListOf()
    ) {
        override fun toString(): String {
            return aClassData.aClass.packagePath
        }
    }
    private val allUseCases = mutableMapOf<String, AClassDataNode>()
    private var allOtherDeps = mapOf<String, AClassDataNode>()

    override fun generate(aState: AState) {

        allOtherDeps = (aState.domainEntities + aState.repos + aState.services).map { it.aClass.packagePath to AClassDataNode(it) }.toMap()

        aState.useCases.forEach { allUseCases[it.aClass.packagePath] = AClassDataNode(aClassData = it) }
        aState.useCases.forEach { it: AClassData ->
            val aClassDataNode = AClassDataNode(aClassData = it)
            it.aProps.forEach {
                println(it.aProp.type)
                val dep = allUseCases[it.aProp.type] ?: allOtherDeps[it.aProp.type]
                if(dep != null) aClassDataNode.dependencies.add(dep)
            }
        }

        //TODO: Build App dependency object.
    }

}
