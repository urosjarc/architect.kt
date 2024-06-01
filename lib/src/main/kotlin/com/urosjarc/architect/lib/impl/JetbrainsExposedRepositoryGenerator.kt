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
    private val mapping: List<Pair<String, Triple<(APropData) -> String, (APropData) -> String, (APropData) -> String>>>
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
        val baseRepoName = "DomainEntityRepo"

        val text = """
        import $pacPath.$clsName
        
        internal interface $baseRepoName<T> {
            public fun insert(obj: T): T?
            public fun insert(objs: Iterable<T>): List<T> 
            public fun update(obj: T): T?
            public fun delete(id: Id<T>): T?
            public fun select(id: Id<T>): T?
            public fun select(): List<T>
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

        val imports = this.generateImports(clsData = clsData)
        val tableFields = this.generateTableFields(clsData = clsData)
        val domainFields = this.generateDomainFields(clsData = clsData)
        val insertFields = this.generateInsertFields(clsData = clsData)
        val batchInsertFields = this.generateBatchInsertFields(clsData = clsData)
        val updateFields = this.generateUpdateFields(clsData = clsData)

        val text = """
        import org.jetbrains.exposed.dao.id.UUIDTable
        import org.jetbrains.exposed.sql.*
        import org.jetbrains.exposed.sql.transactions.transaction
        ${imports.joinToString("\n" + " ".repeat(4 * 2))}
        
        internal abstract class $repoName(private val db: Database) : DomainEntityRepo<$clsName> {
            object table : UUIDTable(name = "$clsName", columnName = "id") {
                ${tableFields.joinToString("\n" + " ".repeat(4 * 4))}
            }
            
            private fun toDomain(row: ResultRow) = $clsName(
                ${domainFields.joinToString("\n" + " ".repeat(4 * 4))}
            )
            
            override fun insert(obj: $clsName): $clsName? {
                return transaction(db) {
                    table.insert {
                        ${insertFields.joinToString("\n" + " ".repeat(4 * 6))}
                    }.resultedValues?.singleOrNull()?.let(::toDomain)
                }
            }
            
            override fun insert(objs: Iterable<$clsName>): List<$clsName> {
                val t = table
                return transaction(db) {
                    t.batchInsert(objs) {
                        ${batchInsertFields.joinToString("\n" + " ".repeat(4 * 6))}
                    }.map(::toDomain)
                }
            }
            
            override fun update(obj: $clsName): $clsName? {
                return transaction(db) {
                    table.updateReturning(where = { table.id eq obj.id.value }) { 
                        ${updateFields.joinToString("\n" + " ".repeat(4 * 6))}
                    }.singleOrNull()?.let(::toDomain)
                }
            } 
            
            override fun delete(id: Id<$clsName>): $clsName? {
                return transaction(db) {
                    table.deleteReturning {
                        table.id eq id.value
                    }.singleOrNull()?.let(::toDomain)
                }
            } 
    
            override fun select(): List<$clsName> {
                return transaction(db) {
                    table.selectAll().map(::toDomain)
                }
            }

            override fun select(id: Id<$clsName>): $clsName? {
                return transaction(db) {
                    table.selectAll()
                        .where { table.id eq id.value }
                        .map(::toDomain)
                        .singleOrNull()
                }
            }
        }
        """.trimIndent()

        File(implementationFolder, "$repoName.kt").apply {
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
            val mappedData = mapping.first { data.aProp.type == it.first }.second.second(data)
            fields.add("${data.aProp.name} = $mappedData,")
        }
        return fields
    }

    private fun generateUpdateFields(clsData: AClassData): Iterable<String> {
        val fields = mutableListOf<String>()
        clsData.aProps.forEach { data: APropData ->
            if (data.aProp.name != "id") {
                val mappedData = mapping.first { data.aProp.type == it.first }.second.third(data)
                fields.add("it[${data.aProp.name}] = obj.${data.aProp.name}${mappedData}")
            }
        }
        return fields
    }

    private fun generateInsertFields(clsData: AClassData): Iterable<String> {
        val fields = mutableListOf<String>()
        clsData.aProps.forEach { data: APropData ->
            val mappedData = mapping.first { data.aProp.type == it.first }.second.third(data)
            fields.add("it[${data.aProp.name}] = obj.${data.aProp.name}${mappedData}")
        }
        return fields
    }

    private fun generateBatchInsertFields(clsData: AClassData): Iterable<String> {
        val fields = mutableListOf<String>()
        clsData.aProps.forEach { data: APropData ->
            val mappedData = mapping.first { data.aProp.type == it.first }.second.third(data)
            fields.add("this[t.${data.aProp.name}] = it.${data.aProp.name}${mappedData}")
        }
        return fields
    }

    private fun generateTableFields(clsData: AClassData): Iterable<String> {
        val fields = mutableListOf<String>()
        clsData.aProps.forEach { data: APropData ->
            if (data.aProp.name != "id") {
                val mappedData = mapping.first { data.aProp.type == it.first }.second.first(data)
                fields.add("val ${data.aProp.name} = $mappedData")
            }
        }
        return fields
    }

}
