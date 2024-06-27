package com.urosjarc.architect.lib.generators

import com.urosjarc.architect.annotations.Mod
import com.urosjarc.architect.annotations.New
import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.data.APropData
import com.urosjarc.architect.lib.data.AStateData
import com.urosjarc.architect.lib.extend.name
import org.apache.logging.log4j.kotlin.logger
import java.io.File

public class DomainModelsGenerator(
    private val modelFolder: File,
) : Generator {

    private val modelPackage = this.modelFolder.absolutePath.split("/kotlin/").last().replace("/", ".")
    override fun generate(aStateData: AStateData) {
        aStateData.domainEntities.forEach { it: AClassData ->
            logger.info(it)
            this.generateModel(clsData = it)
        }
    }

    private fun generateModel(clsData: AClassData) {
        val clsName = clsData.aClass.name

        val newFields = mutableListOf<String>()
        val modFields = mutableListOf<String>()
        val newImportsFields = mutableSetOf<String>()
        val modImportsFields = mutableSetOf<String>()
        clsData.aProps.forEach { data: APropData ->
            var type = data.aProp.type.split(".").last()
            val typeParams = data.aTypeParams.map { it.name }.joinToString(",")

            if (data.aProp.inlineType != null) type += "<${typeParams}>"

            if (data.aProp.annotations.contains(name<New>())) {
                newFields.add("val ${data.aProp.name}: $type,")
                newImportsFields.add(data.aProp.type)
                newImportsFields.addAll(data.aTypeParams.map { it.import })
            }
            if (data.aProp.annotations.contains(name<Mod>())) {
                modFields.add("val ${data.aProp.name}: $type,")
                modImportsFields.add(data.aProp.type)
                modImportsFields.addAll(data.aTypeParams.map { it.import })
            }
        }


        val modText = """
        package $modelPackage
        
        import kotlinx.serialization.Serializable 
        ${modImportsFields.map { "import $it\n" + " ".repeat(2 * 4) }.joinToString("")}
        
        @Serializable
        public data class ${clsName}Mod(
            ${modFields.joinToString("\n" + " ".repeat(3 * 4))}
        )
        """.trimIndent()

        val newText = """
        package $modelPackage
        
        import kotlinx.serialization.Serializable 
        ${newImportsFields.joinToString("") { "import $it\n" + " ".repeat(2 * 4) }}
        
        @Serializable
        public data class ${clsName}New(
            ${newFields.joinToString("\n" + " ".repeat(3 * 4))}
        )
        """.trimIndent()

        println(modFields.size)
        check(modFields.isNotEmpty()) { "@Mod fields are not found in: $clsName" }
        println(newFields.size)
        check(newFields.isNotEmpty()) { "@New fields are not found in: $clsName" }

        File(modelFolder, "${clsName}New.kt").writeText(newText)
        File(modelFolder, "${clsName}Mod.kt").writeText(modText)
    }

}
