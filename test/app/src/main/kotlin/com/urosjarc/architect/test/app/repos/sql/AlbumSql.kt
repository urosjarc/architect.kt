package com.urosjarc.architect.test.app.repos.sql

import com.urosjarc.architect.test.core.repos.Repo
import com.urosjarc.architect.test.core.models.AlbumNew
import com.urosjarc.architect.test.core.models.AlbumMod
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers
import com.urosjarc.architect.test.core.domain.Album
import com.urosjarc.architect.test.core.types.Id
import kotlin.String

public abstract class AlbumSql(private val db: Database) : Repo<Album, AlbumNew, AlbumMod> {
    object table : UUIDTable(name = "Album", columnName = "id") {
        val artistId = reference("artistId", ArtistSql.table)
        val name = varchar("name", 200)
    }
    
    private fun toDomain(row: ResultRow) = Album(
        artistId = Id(row[table.artistId].value),
        id = Id(row[table.id].value),
        name = row[table.name],
    )
   
   internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }
    
    public override suspend fun insert(obj: AlbumNew): Album? {
        return this.transaction {
            table.insert {
                it[artistId] = obj.artistId.value
                it[name] = obj.name
            }.resultedValues?.singleOrNull()?.let(::toDomain)
        }
    }
    
    public override suspend fun insert(objs: Iterable<AlbumNew>): List<Album> {
        val t = table
        return this.transaction {
            t.batchInsert(objs) {
                this[t.artistId] = it.artistId.value
                this[t.name] = it.name
            }.map(::toDomain)
        }
    }
    
    public override suspend fun updateMod(obj: AlbumMod): Album? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun update(obj: Album): Album? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun delete(id: Id<Album>): Album? {
        return this.transaction {
            table.deleteReturning {
                table.id eq id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 

    public override suspend fun select(): List<Album> {
        return this.transaction {
            table.selectAll().map(::toDomain)
        }
    }

    public override suspend fun select(id: Id<Album>): Album? {
        return this.transaction {
            table.selectAll()
                .where { table.id eq id.value }
                .singleOrNull()?.let(::toDomain)
        }
    }
}