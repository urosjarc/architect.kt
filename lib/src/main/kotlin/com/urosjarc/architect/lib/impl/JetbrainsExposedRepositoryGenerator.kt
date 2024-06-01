package com.urosjarc.architect.lib.impl

import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.data.APropData
import com.urosjarc.architect.lib.domain.AState
import java.io.File

public interface Test {
    public fun test(): String
}

public interface Test2 : Test {
    public fun test2(): String
}

public class JetbrainsExposedRepositoryGenerator(
    private val interfaceFolder: File,
    private val sqlFolder: File,
    private val repoFolder: File,
    private val mapping: List<Pair<String, Triple<(APropData) -> String, (APropData) -> String, (APropData) -> String>>>
) : Generator {

    override fun generate(aState: AState) {
        this.generateDomainEntityRepo(clsData = aState.identifiers[0])
        aState.domainEntities.forEach { it: AClassData ->
            this.generateRepo(clsData = it)
            this.generateSql(clsData = it)
            this.generateSqlRepo(clsData = it)
        }
    }

    private fun generateRepo(clsData: AClassData) {
        val pacPath = clsData.aClass.path
        val clsName = clsData.aClass.name
        val repoName = "${clsName}Repo"

        val text = """
        import $pacPath.$clsName
        
        internal interface $repoName : Repo<$clsName> {
        }
        """.trimIndent()

        File(interfaceFolder, "$repoName.kt").writeText(text)
    }

    private fun generateSqlRepo(clsData: AClassData) {
        val pacPath = clsData.aClass.path
        val clsName = clsData.aClass.name
        val repoName = "${clsName}SqlRepo"

        val text = """
        import org.jetbrains.exposed.sql.Database
        import $pacPath.$clsName
        
        public class $repoName(db: Database) : ${clsName}Sql(db=db) {
        }
        """.trimIndent()

        File(repoFolder, "$repoName.kt").writeText(text)
    }

    private fun generateDomainEntityRepo(clsData: AClassData) {
        val pacPath = clsData.aClass.path
        val clsName = clsData.aClass.name
        val baseRepoName = "Repo"

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

        File(interfaceFolder, "$baseRepoName.kt").writeText(text)
    }

    private fun generateSql(clsData: AClassData) {
        val clsName = clsData.aClass.name
        val repoName = "${clsName}Sql"

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
        
        public abstract class $repoName(private val db: Database) : Repo<$clsName> {
            object table : UUIDTable(name = "$clsName", columnName = "id") {
                ${tableFields.joinToString("\n" + " ".repeat(4 * 4))}
            }
            
            private fun toDomain(row: ResultRow) = $clsName(
                ${domainFields.joinToString("\n" + " ".repeat(4 * 4))}
            )
            
            public override fun insert(obj: $clsName): $clsName? {
                return transaction(db) {
                    table.insert {
                        ${insertFields.joinToString("\n" + " ".repeat(4 * 6))}
                    }.resultedValues?.singleOrNull()?.let(::toDomain)
                }
            }
            
            public override fun insert(objs: Iterable<$clsName>): List<$clsName> {
                val t = table
                return transaction(db) {
                    t.batchInsert(objs) {
                        ${batchInsertFields.joinToString("\n" + " ".repeat(4 * 6))}
                    }.map(::toDomain)
                }
            }
            
            public override fun update(obj: $clsName): $clsName? {
                return transaction(db) {
                    table.updateReturning(where = { table.id eq obj.id.value }) { 
                        ${updateFields.joinToString("\n" + " ".repeat(4 * 6))}
                    }.singleOrNull()?.let(::toDomain)
                }
            } 
            
            public override fun delete(id: Id<$clsName>): $clsName? {
                return transaction(db) {
                    table.deleteReturning {
                        table.id eq id.value
                    }.singleOrNull()?.let(::toDomain)
                }
            } 
    
            public override fun select(): List<$clsName> {
                return transaction(db) {
                    table.selectAll().map(::toDomain)
                }
            }

            public override fun select(id: Id<$clsName>): $clsName? {
                return transaction(db) {
                    table.selectAll()
                        .where { table.id eq id.value }
                        .map(::toDomain)
                        .singleOrNull()
                }
            }
        }
        """.trimIndent()

        File(sqlFolder, "$repoName.kt").writeText(text)
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
