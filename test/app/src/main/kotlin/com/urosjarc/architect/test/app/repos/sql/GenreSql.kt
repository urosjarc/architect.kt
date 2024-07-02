package com.urosjarc.architect.test.app.repos.sql

import com.urosjarc.architect.test.core.repos.Repo
import com.urosjarc.architect.test.core.models.GenreNew
import com.urosjarc.architect.test.core.models.GenreMod
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers
import com.urosjarc.architect.test.core.domain.Genre
import com.urosjarc.architect.test.core.types.Id
import kotlin.String

public abstract class GenreSql(private val db: Database) : Repo<Genre, GenreNew, GenreMod> {
    object table : UUIDTable(name = "Genre", columnName = "id") {
        val name = varchar("name", 200)
    }
    
    private fun toDomain(row: ResultRow) = Genre(
        id = Id(row[table.id].value),
        name = row[table.name],
    )
   
   internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }
    
    public override suspend fun insert(obj: GenreNew): Genre? {
        return this.transaction {
            table.insert {
                it[name] = obj.name
            }.resultedValues?.singleOrNull()?.let(::toDomain)
        }
    }
    
    public override suspend fun insert(objs: Iterable<GenreNew>): List<Genre> {
        val t = table
        return this.transaction {
            t.batchInsert(objs) {
                this[t.name] = it.name
            }.map(::toDomain)
        }
    }
    
    public override suspend fun updateMod(obj: GenreMod): Genre? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun update(obj: Genre): Genre? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun delete(id: Id<Genre>): Genre? {
        return this.transaction {
            table.deleteReturning {
                table.id eq id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 

    public override suspend fun select(): List<Genre> {
        return this.transaction {
            table.selectAll().map(::toDomain)
        }
    }

    public override suspend fun select(id: Id<Genre>): Genre? {
        return this.transaction {
            table.selectAll()
                .where { table.id eq id.value }
                .singleOrNull()?.let(::toDomain)
        }
    }
}