package com.urosjarc.architect.lib.generators

import com.urosjarc.architect.lib.Architect
import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AStateData
import com.urosjarc.architect.lib.domain.AVisibility
import com.urosjarc.architect.lib.extend.afterLastDot
import java.io.File

public class PlantUMLDependencySpaceGenerator(
    private val outputFile: File
) : Generator {

    override fun generate(aStateData: AStateData) {
        val rootFolder = Architect.getFolderNodes(aStateData = aStateData)

        val lines = mutableListOf("@startuml", "skinparam backgroundColor darkgray", "skinparam ClassBackgroundColor lightgray")
        val connections = mutableListOf<String>()

        val queue = mutableListOf(rootFolder)
        while (queue.isNotEmpty()) {
            val folderNode = queue.removeFirst()

            folderNode.aClassDatas.forEach { e ->
                lines.add("class ${e.aClass.name} {")
                e.aProps.forEach { p ->
                    lines.add("\t${getVisibility(p.aProp.visibility)}${p.aProp.name}: ${p.aProp.type.afterLastDot}")
                    connections.add("${e.aClass.name} --> ${p.aProp.type.afterLastDot}")
                }
                lines.add("}")
            }

            folderNode.children.forEach { child ->
                queue.add(child)
            }
        }

        connections.add("@enduml")

        val text = (lines + connections).joinToString("\n")
        this.outputFile.writeText(text = text)
    }

    private fun getVisibility(aVisibility: AVisibility): Char {
        return when (aVisibility) {
            AVisibility.PUBLIC -> '+'
            AVisibility.PROTECTED -> '#'
            AVisibility.INTERNAL -> '~'
            AVisibility.PRIVATE -> '-'
        }
    }
}
