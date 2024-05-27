package com.urosjarc.architect.lib.impl

import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.domain.AState
import java.io.File

public class JetbrainsExposedRepositoryGenerator(
    private val interfaceFolder: File,
    private val implementationFolder: File
) : Generator {

    override fun generate(aState: AState) {

        aState.domainEntities.forEach {
            val inteFile = File(interfaceFolder, "${it.aClass.name}Repo.kt")
            val implFile = File(implementationFolder, "${it.aClass.name}SqlRepo.kt")
            inteFile.writeText(this.baseInterface(packagePath = "package.path", idPath = "id.path"))
            implFile.writeText(this.baseImplementation(aClassData = it))
        }
    }

    private fun baseInterface(packagePath: String, idPath: String) = """
        package $packagePath
        
        import $idPath
        
        interface Repo<T> {
            fun getOneById(id: Id<T>): T
            fun getAll(): List<T>
            fun create(obj: T): List<T>
            fun delete(obj: T): Boolean
        }
        """.trimIndent()

    private fun baseImplementation(aClassData: AClassData): String {
        val T = aClassData.aClass.name
        return """
        package com.urosjarc.architect.api.repos.sql
        
        import com.urosjarc.architect.lib.extend.name
        import com.urosjarc.architect.core.domain.User
        import com.urosjarc.architect.core.repos.UserRepo
        import com.urosjarc.architect.core.types.Encrypted
        import com.urosjarc.architect.core.types.Hashed
        import com.urosjarc.architect.lib.types.Id
        import com.urosjarc.architect.core.types.encrypted
        import org.jetbrains.exposed.sql.*
        import org.jetbrains.exposed.sql.transactions.transaction
        
        internal class ${T}SqlRepo(val db: Database) : ${T}Repo {
            ${exposedTable(aClassData = aClassData)}
        
            private fun toDomain(row: ResultRow) =
                $T(
                    id = Id(row[table.id]),
                    email = Encrypted(row[table.email].encodeToByteArray()),
                    password = Hashed(row[table.password].encodeToByteArray()),
                    type = User.Type.valueOf(value = row[table.type]),
                )
        
            override fun getAll(): List<$T> =
                transaction(db) {
                    table.selectAll().map(::toDomain)
                }
        
            override fun get(id: Id<$T>): $T? =
                transaction(db) {
                    table.selectAll()
                        .where { table.id eq id.value }
                        .map(::toDomain)
                        .singleOrNull()
                }
        
            override fun insert(obj: $T) {
                transaction(db) {
                    table.insert {
                        it[id] = obj.id.value
                        it[email] = obj.email.toString()
                        it[password] = obj.password.toString()
                        it[type] = obj.type.name
                    }.resultedValues?.singleOrNull()?.let(::toDomain)
                }
            }
        }
        """.trimIndent()
    }

    private val domainType_to_exposedType = mapOf(
        "String" to "varchar"
    )

    private fun exposedTable(aClassData: AClassData): String {
        val clsName = aClassData.aClass.name
        val lines = mutableListOf(
            "object table : Table(name = ${clsName}::class.simpleName()) {"
        )

        aClassData.aProps.forEach {
            val dbType = domainType_to_exposedType[it.type]
            lines.add("val ${it.name} = $dbType(\"${it.name}\")")
        }
        lines.add("")
        lines.add("override val primaryKey = PrimaryKey(id)")
        lines.add("}")

        return lines.joinToString("\n")
    }

}
