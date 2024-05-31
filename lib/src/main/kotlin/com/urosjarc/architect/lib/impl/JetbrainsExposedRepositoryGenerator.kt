package com.urosjarc.architect.lib.impl

import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.domain.AProp
import com.urosjarc.architect.lib.domain.AState
import org.apache.logging.log4j.kotlin.logger
import java.io.File

public interface Test {
    public fun test(): String
}

public interface Test2 : Test {
    public fun test2(): String
}

public class JetbrainsExposedRepositoryGenerator(
    private val interfaceFolder: File,
    private val implementationFolder: File,
    private val mapping: List<Pair<String, (AProp) -> String>>
) : Generator {


    override fun generate(aState: AState) {

        aState.identificators.forEach { it: AClassData ->
            this.generateBaseRepoInterface(clsData = it)
        }

        aState.domainEntities.forEach { it: AClassData ->
            this.generateBaseSqlRepo(clsData = it)
        }
    }


    private fun generateBaseRepoInterface(clsData: AClassData) {
        val pacPath = clsData.aClass.path
        val clsName = clsData.aClass.name
        val valName = clsName.lowercase()
        val baseRepoName = "${clsName}Repo"

        val text = """
        import $pacPath.$clsName
        
        internal interface $baseRepoName<T> {
            public fun getOneById($valName: $clsName<T>): T
            public fun getAll(): List<T>
            public fun create(obj: T): List<T>
            public fun delete(obj: T): Boolean
        }
        """.trimIndent()

        File(interfaceFolder, "$baseRepoName.kt").apply {
            logger.info(this.absolutePath)
            writeText(text)
        }
    }

    private fun generateBaseSqlRepo(clsData: AClassData) {
        val clsName = clsData.aClass.name
        val valName = clsName.lowercase()
        val lines = mutableListOf(
            "import org.jetbrains.exposed.sql.Table",
            "object table : Table(name = \"$valName\") {"
        )

        clsData.aProps.forEach { aProp ->
            logger.info(aProp)
            val dbType = mapping.first { aProp.type.startsWith(it.first) }.second(aProp)
            lines.add("val ${aProp.name} = $dbType")
        }
        lines.add("")
        lines.add("override val primaryKey = PrimaryKey(id)")
        lines.add("}")

        val text = lines.joinToString("\n")

        File(implementationFolder, "$clsName.kt").apply {
            logger.info(this.absolutePath)
            writeText(text)
        }
    }

}
