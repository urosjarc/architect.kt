package com.urosjarc.architect.lib.impl

import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.data.APropData
import com.urosjarc.architect.lib.domain.AState
import java.io.File

private typealias Mapping = List<Pair<String, Triple<(APropData) -> String, (APropData) -> String, (APropData) -> String>>>

public class JetbrainsExposedRepositoryGenerator(
    private val interfaceFolder: File,
    private val sqlFolder: File,
    private val repoFolder: File,
    mapping: Mapping
) : Generator {

    private val interfacePackage = this.interfaceFolder.absolutePath.split("/kotlin/").last().replace("/", ".")
    private val sqlPackage = this.sqlFolder.absolutePath.split("/kotlin/").last().replace("/", ".")
    private val repoPackage = this.repoFolder.absolutePath.split("/kotlin/").last().replace("/", ".")

    private val mapping: Mapping = mapping + listOf(
        "kotlin.String" to Triple(
            { "varchar(\"${it.aProp.name}\", 200)" },
            { "row[table.${it.aProp.name}]" },
            { "" },
        ),
        "kotlin.Int" to Triple(
            { "integer(\"${it.aProp.name}\")" },
            { "row[table.${it.aProp.name}]" },
            { "" },
        ),
        "kotlin.Float" to Triple(
            { "float(\"${it.aProp.name}\")" },
            { "row[table.${it.aProp.name}]" },
            { "" },
        ),
        "kotlin.Boolean" to Triple(
            { "bool(\"${it.aProp.name}\")" },
            { "row[table.${it.aProp.name}]" },
            { "" },
        ),
    )

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
        package $interfacePackage
        
        import $pacPath.$clsName
        import com.urosjarc.architect.annotations.Repository
        
        @Repository
        public interface $repoName : Repo<$clsName> {
        }
        """.trimIndent()

        File(interfaceFolder, "$repoName.kt").writeText(text)
    }

    private fun generateSqlRepo(clsData: AClassData) {
        val pacPath = clsData.aClass.path
        val clsName = clsData.aClass.name
        val repoName = "${clsName}SqlRepo"

        val text = """
        package $repoPackage
        
        import org.jetbrains.exposed.sql.Database
        import $sqlPackage.${clsName}Sql
        
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
        package $interfacePackage
        
        import $pacPath.$clsName
        
        public interface $baseRepoName<T> {
            public suspend fun insert(obj: T): T?
            public suspend fun insert(objs: Iterable<T>): List<T> 
            public suspend fun update(obj: T): T?
            public suspend fun delete(id: Id<T>): T?
            public suspend fun select(id: Id<T>): T?
            public suspend fun select(): List<T>
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
        package $sqlPackage
        
        import $interfacePackage.Repo
        import org.jetbrains.exposed.dao.id.UUIDTable
        import org.jetbrains.exposed.sql.*
        import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
        import kotlinx.coroutines.Dispatchers
        ${imports.joinToString("\n" + " ".repeat(4 * 2))}
        
        public abstract class $repoName(private val db: Database) : Repo<$clsName> {
            object table : UUIDTable(name = "$clsName", columnName = "id") {
                ${tableFields.joinToString("\n" + " ".repeat(4 * 4))}
            }
            
            private fun toDomain(row: ResultRow) = $clsName(
                ${domainFields.joinToString("\n" + " ".repeat(4 * 4))}
            )
           
           internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }
            
            public override suspend fun insert(obj: $clsName): $clsName? {
                return this.transaction {
                    table.insert {
                        ${insertFields.joinToString("\n" + " ".repeat(4 * 6))}
                    }.resultedValues?.singleOrNull()?.let(::toDomain)
                }
            }
            
            public override suspend fun insert(objs: Iterable<$clsName>): List<$clsName> {
                val t = table
                return this.transaction {
                    t.batchInsert(objs) {
                        ${batchInsertFields.joinToString("\n" + " ".repeat(4 * 6))}
                    }.map(::toDomain)
                }
            }
            
            public override suspend fun update(obj: $clsName): $clsName? {
                return this.transaction {
                    table.updateReturning(where = { table.id eq obj.id.value }) { 
                        ${updateFields.joinToString("\n" + " ".repeat(4 * 6))}
                    }.singleOrNull()?.let(::toDomain)
                }
            } 
            
            public override suspend fun delete(id: Id<$clsName>): $clsName? {
                return this.transaction {
                    table.deleteReturning {
                        table.id eq id.value
                    }.singleOrNull()?.let(::toDomain)
                }
            } 
    
            public override suspend fun select(): List<$clsName> {
                return this.transaction {
                    table.selectAll().map(::toDomain)
                }
            }

            public override suspend fun select(id: Id<$clsName>): $clsName? {
                return this.transaction {
                    table.selectAll()
                        .where { table.id eq id.value }
                        .singleOrNull()?.let(::toDomain)
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
