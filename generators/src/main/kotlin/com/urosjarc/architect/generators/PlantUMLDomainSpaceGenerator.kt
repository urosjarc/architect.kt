package com.urosjarc.architect.generators

import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AStateData
import com.urosjarc.architect.lib.extend.ext_afterLastDot
import com.urosjarc.architect.lib.extend.ext_firstLine
import java.io.File

public class PlantUMLDomainSpaceGenerator(
    private val outputFile: File
) : Generator {

    override fun generate(aStateData: AStateData) {
        outputFile.createNewFile()

        val lines = mutableListOf("@startuml", "skinparam backgroundColor darkgray", "skinparam ClassBackgroundColor lightgray")
        val connections = mutableListOf<String>()

        val pacPath_to_domainEntity = aStateData.domainEntities.associateBy { it.aClass.import }

        aStateData.domainEntities.forEach { e ->
            lines.add("class ${e.aClass.name} {")

            e.aClass.docs?.let {
                lines.add("\t${it.ext_firstLine}")
                lines.add("\t==")
            }

            e.aProps.forEach { p ->
                var simpleType = p.aProp.type.ext_afterLastDot
                if (p.aTypeParams.isNotEmpty()) simpleType += "<${p.aTypeParams.joinToString { it.name }}>"
                lines.add("\t${PlantUML.getVisibility(p.aProp.visibility)}${p.aProp.name}: $simpleType")

                p.aTypeParams.forEach { tp ->
                    val con = pacPath_to_domainEntity[tp.import]
                    if (con != null && !p.aProp.isIdentifier) {
                        connections.add("${e.aClass.name} -up-> ${con.aClass.name}: ${p.aProp.name}")
                    }
                }
            }

            lines.addAll(PlantUML.getMethods(entity = e))


            lines.add("}")

        }

        aStateData.domainValues.forEach { e ->
            lines.add("enum ${e.aEnum.name} {")

            e.aEnum.docs?.let {
                lines.add("\t${it.ext_firstLine}")
                lines.add("\t==")
            }

            e.aEnumValues.forEach { p ->
                lines.add("\t${p.name}: ${p.value}")
                p.docs?.let { lines.add("\t$it") }
                p.docs?.let { lines.add("\t==") }
            }
            lines.add("}")
        }

        connections.add("@enduml")

        val text = (lines + connections).joinToString("\n")
        this.outputFile.writeText(text = text)
    }
}
