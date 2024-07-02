package com.urosjarc.architect.test.app.repos.sql

import com.urosjarc.architect.test.core.repos.Repo
import com.urosjarc.architect.test.core.models.PlaylistTrackNew
import com.urosjarc.architect.test.core.models.PlaylistTrackMod
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers
import com.urosjarc.architect.test.core.domain.PlaylistTrack
import com.urosjarc.architect.test.core.types.Id

public abstract class PlaylistTrackSql(private val db: Database) : Repo<PlaylistTrack, PlaylistTrackNew, PlaylistTrackMod> {
    object table : UUIDTable(name = "PlaylistTrack", columnName = "id") {
        val playlistId = reference("playlistId", PlaylistSql.table)
        val trackId = reference("trackId", TrackSql.table)
    }
    
    private fun toDomain(row: ResultRow) = PlaylistTrack(
        id = Id(row[table.id].value),
        playlistId = Id(row[table.playlistId].value),
        trackId = Id(row[table.trackId].value),
    )
   
   internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }
    
    public override suspend fun insert(obj: PlaylistTrackNew): PlaylistTrack? {
        return this.transaction {
            table.insert {
                it[playlistId] = obj.playlistId.value
                it[trackId] = obj.trackId.value
            }.resultedValues?.singleOrNull()?.let(::toDomain)
        }
    }
    
    public override suspend fun insert(objs: Iterable<PlaylistTrackNew>): List<PlaylistTrack> {
        val t = table
        return this.transaction {
            t.batchInsert(objs) {
                this[t.playlistId] = it.playlistId.value
                this[t.trackId] = it.trackId.value
            }.map(::toDomain)
        }
    }
    
    public override suspend fun updateMod(obj: PlaylistTrackMod): PlaylistTrack? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun update(obj: PlaylistTrack): PlaylistTrack? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun delete(id: Id<PlaylistTrack>): PlaylistTrack? {
        return this.transaction {
            table.deleteReturning {
                table.id eq id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 

    public override suspend fun select(): List<PlaylistTrack> {
        return this.transaction {
            table.selectAll().map(::toDomain)
        }
    }

    public override suspend fun select(id: Id<PlaylistTrack>): PlaylistTrack? {
        return this.transaction {
            table.selectAll()
                .where { table.id eq id.value }
                .singleOrNull()?.let(::toDomain)
        }
    }
}