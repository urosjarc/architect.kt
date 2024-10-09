package com.urosjarc.architect.lib.generators

import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.data.APropData
import com.urosjarc.architect.lib.data.AStateData
import java.io.File

public data class JetbrainsExposedTypeMapping(
    val tableFieldDefinition: (APropData) -> String,
    val toDomain: (APropData) -> String,
    val domainValue: (APropData) -> String
)

public typealias MappingData = Pair<String, JetbrainsExposedTypeMapping>
public typealias Mapping = List<MappingData>

public class JetbrainsExposedRepositoryGenerator(
    private val interfaceFolder: File,
    private val sqlFolder: File,
    private val repoFolder: File,
    modelFolder: File,
    mapping: Mapping
) : Generator {

    private val domainModelGen = DomainModelsGenerator(modelFolder = modelFolder)
    private val interfacePackage = this.interfaceFolder.absolutePath.split("/kotlin/").last().replace("/", ".")
    private val sqlPackage = this.sqlFolder.absolutePath.split("/kotlin/").last().replace("/", ".")
    private val repoPackage = this.repoFolder.absolutePath.split("/kotlin/").last().replace("/", ".")

    private val mapping: Mapping = mapping + listOf(
        "kotlin.String" to JetbrainsExposedTypeMapping(
            { "varchar(\"${it.aProp.name}\", 200)" },
            { "row[table.${it.aProp.name}]" },
            { "" },
        ),
        "kotlin.Int" to JetbrainsExposedTypeMapping(
            { "integer(\"${it.aProp.name}\")" },
            { "row[table.${it.aProp.name}]" },
            { "" },
        ),
        "kotlin.Float" to JetbrainsExposedTypeMapping(
            { "float(\"${it.aProp.name}\")" },
            { "row[table.${it.aProp.name}]" },
            { "" },
        ),
        "kotlin.Boolean" to JetbrainsExposedTypeMapping(
            { "bool(\"${it.aProp.name}\")" },
            { "row[table.${it.aProp.name}]" },
            { "" },
        ),
    )

    override fun generate(aStateData: AStateData) {

        this.generateDomainEntityRepo(clsData = aStateData.identifiers[0])
        aStateData.domainEntities.forEach { it: AClassData ->
            this.generateRepo(clsData = it)
            this.generateSql(clsData = it)
            this.generateSqlRepo(clsData = it)
        }

        this.domainModelGen.generate(aStateData = aStateData)
    }

    private fun generateRepo(clsData: AClassData) {
        val clsName = clsData.aClass.name
        val repoName = "${clsName}Repo"

        val text = """
        package $interfacePackage
        
        import ${clsData.aClass.import}
        import ${domainModelGen.modelPackage}.${clsName}New
        import ${domainModelGen.modelPackage}.${clsName}Mod
        import com.urosjarc.architect.annotations.Repository
        
        @Repository
        public interface $repoName : Repo<$clsName, ${clsName}New, ${clsName}Mod> {
        }
        """.trimIndent()

        File(interfaceFolder, "$repoName.kt").writeText(text)
    }

    private fun generateSqlRepo(clsData: AClassData) {
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
        val pacPath = clsData.aClass.packagePath
        val clsName = clsData.aClass.name
        val baseRepoName = "Repo"

        val text = """
        package $interfacePackage
        
        import $pacPath.$clsName
        
        public interface $baseRepoName<T,N,M> {
            public suspend fun insert(obj: N): T?
            public suspend fun insert(objs: Iterable<N>): List<T> 
            public suspend fun updateMod(obj: M): T?
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
        import ${domainModelGen.modelPackage}.${clsName}New
        import ${domainModelGen.modelPackage}.${clsName}Mod
        import org.jetbrains.exposed.dao.id.UUIDTable
        import org.jetbrains.exposed.sql.*
        import org.jetbrains.exposed.sql.kotlin.datetime.*
        import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
        import kotlinx.coroutines.Dispatchers
        ${imports.joinToString("\n" + " ".repeat(4 * 2))}
        
        public abstract class $repoName(private val db: Database) : Repo<$clsName, ${clsName}New, ${clsName}Mod> {
            object table : UUIDTable(name = "$clsName", columnName = "id") {
                ${tableFields.joinToString("\n" + " ".repeat(4 * 4))}
            }
            
            private fun toDomain(row: ResultRow) = $clsName(
                ${domainFields.joinToString("\n" + " ".repeat(4 * 4))}
            )
           
           internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }
            
            public override suspend fun insert(obj: ${clsName}New): $clsName? {
                return this.transaction {
                    table.insert {
                        ${insertFields.joinToString("\n" + " ".repeat(4 * 6))}
                    }.resultedValues?.singleOrNull()?.let(::toDomain)
                }
            }
            
            public override suspend fun insert(objs: Iterable<${clsName}New>): List<$clsName> {
                val t = table
                return this.transaction {
                    t.batchInsert(objs) {
                        ${batchInsertFields.joinToString("\n" + " ".repeat(4 * 6))}
                    }.map(::toDomain)
                }
            }
            
            public override suspend fun updateMod(obj: ${clsName}Mod): $clsName? {
                return this.transaction {
                    table.updateReturning(where = { table.id eq obj.id.value }) { 
                        ${updateFields.joinToString("\n" + " ".repeat(4 * 6))}
                    }.singleOrNull()?.let(::toDomain)
                }
            } 
            
            public override suspend fun update(obj: ${clsName}): $clsName? {
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
        val imports = mutableSetOf(clsData.aClass.import)
        clsData.aProps.forEach { imports.add(it.aProp.type) }
        return imports.map { "import $it" }
    }

    private fun generateDomainFields(clsData: AClassData): Iterable<String> {
        val fields = mutableListOf<String>()
        clsData.aProps.forEach { data: APropData ->
            val mappedData = this.getMappingValue(import = data.aProp.import).second.toDomain(data)
            fields.add("${data.aProp.name} = $mappedData,")
        }
        return fields
    }


    private fun generateUpdateFields(clsData: AClassData): Iterable<String> {
        val fields = mutableListOf<String>()
        clsData.aProps.forEach { data: APropData ->
            if (data.aProp.isMutable || data.aProp.isIdentifier) {
                val mappedData = this.getMappingValue(import = data.aProp.import).second.domainValue(data)
                fields.add("it[${data.aProp.name}] = obj.${data.aProp.name}${mappedData}")
            }
        }
        return fields
    }

    private fun generateInsertFields(clsData: AClassData): Iterable<String> {
        val fields = mutableListOf<String>()
        clsData.aProps.forEach { data: APropData ->
            if (!data.aProp.isOptional) {
                val mappedData = this.getMappingValue(import = data.aProp.import).second.domainValue(data)
                fields.add("it[${data.aProp.name}] = obj.${data.aProp.name}${mappedData}")
            }
        }
        return fields
    }

    private fun generateBatchInsertFields(clsData: AClassData): Iterable<String> {
        val fields = mutableListOf<String>()
        clsData.aProps.forEach { data: APropData ->
            if (!data.aProp.isOptional) {
                val mappedData = this.getMappingValue(import = data.aProp.import).second.domainValue(data)
                fields.add("this[t.${data.aProp.name}] = it.${data.aProp.name}${mappedData}")
            }
        }
        return fields
    }

    private fun generateTableFields(clsData: AClassData): Iterable<String> {
        val fields = mutableListOf<String>()
        clsData.aProps.forEach { data: APropData ->
            if (!data.aProp.isIdentifier) {
                val mappedData = this.getMappingValue(import = data.aProp.import).second.tableFieldDefinition(data)
                fields.add("val ${data.aProp.name} = $mappedData")
            }
        }
        return fields
    }

    private fun getMappingValue(import: String): MappingData {
        return mapping.firstOrNull { import == it.first }
            ?: throw IllegalStateException("Could not found mapping for type: $import")
    }
}
