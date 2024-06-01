package com.urosjarc.architect.lib.impl

import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.data.APropData
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
    private val mapping: List<Pair<String, Pair<(APropData) -> String, (APropData) -> String>>>
) : Generator {


    override fun generate(aState: AState) {

        aState.identifiers.forEach { it: AClassData ->
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
        val repoName = "${clsName}SqlRepo"
        val tableFields = this.generateTableFields(clsData = clsData)
        val domainFields = this.generateDomainFields(clsData = clsData)
        val imports = this.generateImports(clsData = clsData)

        val text = """
        import org.jetbrains.exposed.dao.id.UUIDTable
        import org.jetbrains.exposed.sql.Database
        import org.jetbrains.exposed.sql.ResultRow
        ${imports.joinToString("\n" + " ".repeat(4 * 2))}
        
        internal abstract class $repoName(private val db: Database): UIdRepo<$clsName> {
            object table: UUIDTable(name = "$clsName", columnName="id") {
                ${tableFields.joinToString("\n" + " ".repeat(4 * 4))}
            }
            
            private fun toDomain(row: ResultRow) = $clsName(
                ${domainFields.joinToString("\n" + " ".repeat(4 * 4))}
            )
        }
        """.trimIndent()

        File(implementationFolder, "$clsName.kt").apply {
            logger.info(this.absolutePath)
            writeText(text)
        }
    }

    private fun generateImports(clsData: AClassData): Iterable<String> {
        val imports = mutableSetOf(clsData.aClass.packagePath)
        clsData.aProps.forEach { imports.add(it.aProp.type) }
        return imports.map { "import $it" }
    }

    private fun generateDomainFields(clsData: AClassData): Iterable<String> {
        val fields = mutableListOf<String>()
        clsData.aProps.forEach { data: APropData ->
            val dbType = mapping.first { data.aProp.type == it.first }.second.second(data)
            fields.add("${data.aProp.name} = $dbType,")
        }

        return fields
    }

    private fun generateTableFields(clsData: AClassData): Iterable<String> {
        val fields = mutableListOf<String>()
        clsData.aProps.forEach { data: APropData ->
            if (data.aProp.name != "id") {
                val dbType = mapping.first { data.aProp.type == it.first }.second.first(data)
                fields.add("val ${data.aProp.name} = $dbType")
            }
        }

        return fields
    }

}
