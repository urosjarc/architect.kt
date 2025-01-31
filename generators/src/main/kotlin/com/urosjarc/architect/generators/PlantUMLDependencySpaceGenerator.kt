package com.urosjarc.architect.generators

import com.urosjarc.architect.lib.Architect
import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AStateData
import com.urosjarc.architect.lib.extend.ext_afterLastDot
import com.urosjarc.architect.lib.extend.ext_firstLine
import java.io.File

public class PlantUMLDependencySpaceGenerator(
    private val outputFile: File
) : Generator {

    override fun generate(aStateData: AStateData) {
        outputFile.createNewFile()

        val rootFolder = Architect.getFolderNodes(aStateData = aStateData)

        val lines = mutableListOf("@startuml", "skinparam backgroundColor darkgray", "skinparam ClassBackgroundColor lightgray")
        val connections = mutableListOf<String>()

        val queue = mutableListOf(rootFolder)
        while (queue.isNotEmpty()) {
            val folderNode = queue.removeFirst()

            folderNode.aClassDatas.forEach { e ->
                val type = if(e.aClass.isInterface) "interface" else "class"
                lines.add("$type ${e.aClass.name} {")
                e.aClass.docs?.let {
                    lines.add("\t${it.ext_firstLine}")
                    lines.add("\t==")
                }
                e.aProps.forEach { p ->
                    lines.add("\t${PlantUML.getVisibility(p.aProp.visibility)}${p.aProp.name}: ${p.aProp.type.ext_afterLastDot}")
                    connections.add("${e.aClass.name} --> ${p.aProp.type.ext_afterLastDot}")
                }
                lines.addAll(PlantUML.getMethods(entity = e))
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

}
