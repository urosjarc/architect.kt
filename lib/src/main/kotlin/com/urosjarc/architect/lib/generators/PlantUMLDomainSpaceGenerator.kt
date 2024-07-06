package com.urosjarc.architect.lib.generators

import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AStateData
import com.urosjarc.architect.lib.domain.AState
import com.urosjarc.architect.lib.domain.AVisibility
import com.urosjarc.architect.lib.extend.afterLastDot
import java.io.File

public class PlantUMLDomainSpaceGenerator(
    private val outputFile: File
) : Generator {

    override fun generate(aStateData: AStateData) {
        val lines = mutableListOf("@startuml", "skinparam backgroundColor darkgray", "skinparam ClassBackgroundColor lightgray")
        val connections = mutableListOf<String>()

        val pacPath_to_domainEntity = aStateData.domainEntities.associateBy { it.aClass.import }

        aStateData.domainEntities.forEach { e ->
            lines.add("class ${e.aClass.name} {")

            e.aProps.forEach { p ->
                var type = p.aProp.type.afterLastDot
                if(p.aTypeParams.isNotEmpty()) type += "<${p.aTypeParams.joinToString { it.name }}>"
                lines.add("\t${getVisibility(p.aProp.visibility)}${p.aProp.name}: $type")
                p.aTypeParams.forEach { tp ->
                    val con = pacPath_to_domainEntity[tp.import]
                    if (con != null && p.aProp.name != "id") {
                        connections.add("${e.aClass.name} --> ${con.aClass.name}: ${p.aProp.name}")
                    }
                }
            }

            e.aMethods.filter { m ->
                !m.aMethod.name.contains("[$0-9]".toRegex()) && !listOf(
                    "constructor", "copy", "equals", "toString", "hashCode"
                ).contains(m.aMethod.name)
            }.forEach { m ->
                val params = mutableListOf<String>()
                m.aParams.forEach { p ->
                    val type = p.type.afterLastDot
                    params.add("${p.name}: $type")
                }
                val type = m.aMethod.returnType.afterLastDot
                lines.add("\t${getVisibility(m.aMethod.visibility)}${m.aMethod.name}(${params.joinToString()}) : $type")
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
