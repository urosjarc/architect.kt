package com.urosjarc.architect.test.app.repos.sql

import com.urosjarc.architect.test.core.repos.Repo
import com.urosjarc.architect.test.core.models.TrackNew
import com.urosjarc.architect.test.core.models.TrackMod
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers
import com.urosjarc.architect.test.core.domain.Track
import com.urosjarc.architect.test.core.types.Id
import kotlin.Int
import kotlin.String
import kotlin.Float

public abstract class TrackSql(private val db: Database) : Repo<Track, TrackNew, TrackMod> {
    object table : UUIDTable(name = "Track", columnName = "id") {
        val albumId = reference("albumId", AlbumSql.table)
        val bytes = integer("bytes")
        val composer = varchar("composer", 200)
        val genreId = reference("genreId", GenreSql.table)
        val mediaTypeId = reference("mediaTypeId", MediaTypeSql.table)
        val miliseconds = integer("miliseconds")
        val name = varchar("name", 200)
        val unitPrice = float("unitPrice")
    }
    
    private fun toDomain(row: ResultRow) = Track(
        albumId = Id(row[table.albumId].value),
        bytes = row[table.bytes],
        composer = row[table.composer],
        genreId = Id(row[table.genreId].value),
        id = Id(row[table.id].value),
        mediaTypeId = Id(row[table.mediaTypeId].value),
        miliseconds = row[table.miliseconds],
        name = row[table.name],
        unitPrice = row[table.unitPrice],
    )
   
   internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }
    
    public override suspend fun insert(obj: TrackNew): Track? {
        return this.transaction {
            table.insert {
                it[albumId] = obj.albumId.value
                it[bytes] = obj.bytes
                it[composer] = obj.composer
                it[genreId] = obj.genreId.value
                it[mediaTypeId] = obj.mediaTypeId.value
                it[miliseconds] = obj.miliseconds
                it[name] = obj.name
                it[unitPrice] = obj.unitPrice
            }.resultedValues?.singleOrNull()?.let(::toDomain)
        }
    }
    
    public override suspend fun insert(objs: Iterable<TrackNew>): List<Track> {
        val t = table
        return this.transaction {
            t.batchInsert(objs) {
                this[t.albumId] = it.albumId.value
                this[t.bytes] = it.bytes
                this[t.composer] = it.composer
                this[t.genreId] = it.genreId.value
                this[t.mediaTypeId] = it.mediaTypeId.value
                this[t.miliseconds] = it.miliseconds
                this[t.name] = it.name
                this[t.unitPrice] = it.unitPrice
            }.map(::toDomain)
        }
    }
    
    public override suspend fun updateMod(obj: TrackMod): Track? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun update(obj: Track): Track? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun delete(id: Id<Track>): Track? {
        return this.transaction {
            table.deleteReturning {
                table.id eq id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 

    public override suspend fun select(): List<Track> {
        return this.transaction {
            table.selectAll().map(::toDomain)
        }
    }

    public override suspend fun select(id: Id<Track>): Track? {
        return this.transaction {
            table.selectAll()
                .where { table.id eq id.value }
                .singleOrNull()?.let(::toDomain)
        }
    }
}