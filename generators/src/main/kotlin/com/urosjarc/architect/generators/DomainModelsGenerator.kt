package com.urosjarc.architect.generators

import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.data.APropData
import com.urosjarc.architect.lib.data.AStateData
import com.urosjarc.architect.lib.extend.ext_afterLastDot
import org.apache.logging.log4j.kotlin.logger
import java.io.File

public class DomainModelsGenerator(
    private val modelFolder: File,
) : Generator {

    internal val modelPackage = this.modelFolder.absolutePath.split("/kotlin/").last().replace("/", ".")
    override fun generate(aStateData: AStateData) {
        modelFolder.mkdirs()
        aStateData.domainEntities.forEach { it: AClassData ->
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
            var type = data.aProp.type.ext_afterLastDot
            val typeParams = data.aTypeParams.joinToString { it.name }

            if (data.aProp.inlineType != null) type += "<${typeParams}>"

            if (data.aProp.isNew) {
                newFields.add("val ${data.aProp.name}: $type,")
                newImportsFields.add(data.aProp.import)
                newImportsFields.addAll(data.aTypeParams.map { it.import })
            }
            if (data.aProp.isMod) {
                modFields.add("val ${data.aProp.name}: $type,")
                modImportsFields.add(data.aProp.import)
                modImportsFields.addAll(data.aTypeParams.map { it.import })
            }
        }


        var modText = """
        package $modelPackage
        
        import kotlinx.serialization.Serializable 
        ${modImportsFields.map { "import $it\n" + " ".repeat(2 * 4) }.joinToString("")}
        
        @Serializable
        public data class ${clsName}Mod(
            ${modFields.joinToString("\n" + " ".repeat(3 * 4))}
        )
        """.trimIndent()

        var newText = """
        package $modelPackage
        
        import kotlinx.serialization.Serializable 
        ${newImportsFields.joinToString("") { "import $it\n" + " ".repeat(2 * 4) }}
        
        @Serializable
        public data class ${clsName}New(
            ${newFields.joinToString("\n" + " ".repeat(3 * 4))}
        )
        """.trimIndent()

        if (modImportsFields.isEmpty()) modText = modText.replace("data class", "class")
        if (newImportsFields.isEmpty()) newText = newText.replace("data class", "class")

        File(modelFolder, "${clsName}New.kt").writeText(newText)
        File(modelFolder, "${clsName}Mod.kt").writeText(modText)
    }

}
