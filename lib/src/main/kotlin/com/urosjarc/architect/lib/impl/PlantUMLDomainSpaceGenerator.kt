package com.urosjarc.architect.lib.impl

import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.domain.AState
import com.urosjarc.architect.lib.domain.AVisibility
import com.urosjarc.architect.lib.extend.afterLastDot
import java.io.File

public class PlantUMLDomainSpaceGenerator(
    private val outputFile: File
) : Generator {

    override fun generate(aState: AState) {
        val lines = mutableListOf("@startuml", "skinparam backgroundColor darkgray", "skinparam ClassBackgroundColor lightgray")
        val connections = mutableListOf<String>()

        val pacPath_to_domainEntity = aState.domainEntities.associateBy { it.aClass.packagePath }

        aState.domainEntities.forEach { e ->
            lines.add("class ${e.aClass.name} {")
            e.aProps.forEach { p ->
                lines.add("\t${getVisibility(p.aProp.visibility)}${p.aProp.name}: ${p.aProp.type.afterLastDot}")
                p.aTypeParams.forEach { tp ->
                    val con = pacPath_to_domainEntity[tp.packagePath]
                    if (con != null && p.aProp.name != "id") {
                        connections.add("${e.aClass.name} --> ${con.aClass.name}: ${p.aProp.name}")
                    }
                }
            }
            lines.add("}")
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
