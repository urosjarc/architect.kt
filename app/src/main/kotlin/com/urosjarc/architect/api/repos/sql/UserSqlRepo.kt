package com.urosjarc.architect.api.repos.sql

import com.urosjarc.architect.core.domain.User
import com.urosjarc.architect.core.repos.UserRepo
import com.urosjarc.architect.core.types.Encrypted
import com.urosjarc.architect.core.types.Hashed
import com.urosjarc.architect.core.types.encrypted
import com.urosjarc.architect.core.types.Id
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal class UserSqlRepo(val db: Database) : UserRepo {
    object table : Table(name = "users") {
        val id = uuid("id")
        val email = varchar("email", 128)
        val password = varchar("password", 1024)
        val type = varchar("type", 15)

        override val primaryKey = PrimaryKey(id)
    }

    private fun toDomain(row: ResultRow) =
        User(
            id = Id(row[table.id]),
            email = Encrypted(row[table.email].encodeToByteArray()),
            password = Hashed(row[table.password].encodeToByteArray()),
            type = User.Type.valueOf(value = row[table.type]),
        )

    override fun getAll(): List<User> =
        transaction(db) {
            table.selectAll().map(::toDomain)
        }

    override fun get(id: Id<User>): User? =
        transaction(db) {
            table.selectAll()
                .where { table.id eq id.value }
                .map(::toDomain)
                .singleOrNull()
        }

    override fun find(email: String): List<User> {
        return transaction(db) {
            table.selectAll()
                .where { table.email eq email.encrypted().toString() }
                .map(::toDomain)
        }
    }

    override fun insert(user: User) {
        transaction(db) {
            table.insert {
                it[id] = user.id.value
                it[email] = user.email.toString()
                it[password] = user.password.toString()
                it[type] = user.type.name
            }.resultedValues?.singleOrNull()?.let(::toDomain)
        }
    }
}
