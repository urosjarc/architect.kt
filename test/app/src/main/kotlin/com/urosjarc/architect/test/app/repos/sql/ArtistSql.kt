package com.urosjarc.architect.test.app.repos.sql

import com.urosjarc.architect.test.core.repos.Repo
import com.urosjarc.architect.test.core.models.ArtistNew
import com.urosjarc.architect.test.core.models.ArtistMod
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers
import com.urosjarc.architect.test.core.domain.Artist
import com.urosjarc.architect.test.core.types.Id
import kotlin.String

public abstract class ArtistSql(private val db: Database) : Repo<Artist, ArtistNew, ArtistMod> {
    object table : UUIDTable(name = "Artist", columnName = "id") {
        val name = varchar("name", 200)
    }
    
    private fun toDomain(row: ResultRow) = Artist(
        id = Id(row[table.id].value),
        name = row[table.name],
    )
   
   internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }
    
    public override suspend fun insert(obj: ArtistNew): Artist? {
        return this.transaction {
            table.insert {
                it[name] = obj.name
            }.resultedValues?.singleOrNull()?.let(::toDomain)
        }
    }
    
    public override suspend fun insert(objs: Iterable<ArtistNew>): List<Artist> {
        val t = table
        return this.transaction {
            t.batchInsert(objs) {
                this[t.name] = it.name
            }.map(::toDomain)
        }
    }
    
    public override suspend fun updateMod(obj: ArtistMod): Artist? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
                it[name] = obj.name
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun update(obj: Artist): Artist? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
                it[name] = obj.name
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun delete(id: Id<Artist>): Artist? {
        return this.transaction {
            table.deleteReturning {
                table.id eq id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 

    public override suspend fun select(): List<Artist> {
        return this.transaction {
            table.selectAll().map(::toDomain)
        }
    }

    public override suspend fun select(id: Id<Artist>): Artist? {
        return this.transaction {
            table.selectAll()
                .where { table.id eq id.value }
                .singleOrNull()?.let(::toDomain)
        }
    }
}