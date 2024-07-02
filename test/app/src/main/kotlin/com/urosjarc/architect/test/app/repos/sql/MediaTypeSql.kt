package com.urosjarc.architect.test.app.repos.sql

import com.urosjarc.architect.test.core.repos.Repo
import com.urosjarc.architect.test.core.models.MediaTypeNew
import com.urosjarc.architect.test.core.models.MediaTypeMod
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers
import com.urosjarc.architect.test.core.domain.MediaType
import com.urosjarc.architect.test.core.types.Id
import kotlin.String

public abstract class MediaTypeSql(private val db: Database) : Repo<MediaType, MediaTypeNew, MediaTypeMod> {
    object table : UUIDTable(name = "MediaType", columnName = "id") {
        val mediaTypeId = reference("mediaTypeId", MediaTypeSql.table)
        val name = varchar("name", 200)
    }
    
    private fun toDomain(row: ResultRow) = MediaType(
        id = Id(row[table.id].value),
        mediaTypeId = Id(row[table.mediaTypeId].value),
        name = row[table.name],
    )
   
   internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }
    
    public override suspend fun insert(obj: MediaTypeNew): MediaType? {
        return this.transaction {
            table.insert {
                it[mediaTypeId] = obj.mediaTypeId.value
                it[name] = obj.name
            }.resultedValues?.singleOrNull()?.let(::toDomain)
        }
    }
    
    public override suspend fun insert(objs: Iterable<MediaTypeNew>): List<MediaType> {
        val t = table
        return this.transaction {
            t.batchInsert(objs) {
                this[t.mediaTypeId] = it.mediaTypeId.value
                this[t.name] = it.name
            }.map(::toDomain)
        }
    }
    
    public override suspend fun updateMod(obj: MediaTypeMod): MediaType? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun update(obj: MediaType): MediaType? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun delete(id: Id<MediaType>): MediaType? {
        return this.transaction {
            table.deleteReturning {
                table.id eq id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 

    public override suspend fun select(): List<MediaType> {
        return this.transaction {
            table.selectAll().map(::toDomain)
        }
    }

    public override suspend fun select(id: Id<MediaType>): MediaType? {
        return this.transaction {
            table.selectAll()
                .where { table.id eq id.value }
                .singleOrNull()?.let(::toDomain)
        }
    }
}