package com.urosjarc.architect.example.sql

import com.urosjarc.architect.example.interfaces.Repo
import com.urosjarc.architect.example.models.ParentNew
import com.urosjarc.architect.example.models.ParentMod
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers
import com.urosjarc.architect.example.domain.Parent
import kotlin.String
import kotlinx.datetime.Instant
import com.urosjarc.architect.example.types.Id
import com.urosjarc.architect.example.domain.Sex

public abstract class ParentSql(private val db: Database) : Repo<Parent, ParentNew, ParentMod> {
    object table : UUIDTable(name = "Parent", columnName = "id") {
        val name = varchar("name", 200)
        val surname = varchar("surname", 200)
        val birth = varchar("birth", 200)
        val parent = reference("parent", GrandParentSql.table)
        val sex = varchar("sex", 200)
    }
    
    private fun toDomain(row: ResultRow) = Parent(
        name = row[table.name],
        surname = row[table.surname],
        birth = Instant.parse(row[table.birth]),
        parent = Id(row[table.parent].value),
        sex = Sex.valueOf(row[table.sex]),
        id = Id(row[table.id].value),
    )
   
   internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }
    
    public override suspend fun insert(obj: ParentNew): Parent? {
        return this.transaction {
            table.insert {
                
            }.resultedValues?.singleOrNull()?.let(::toDomain)
        }
    }
    
    public override suspend fun insert(objs: Iterable<ParentNew>): List<Parent> {
        val t = table
        return this.transaction {
            t.batchInsert(objs) {
                
            }.map(::toDomain)
        }
    }
    
    public override suspend fun updateMod(obj: ParentMod): Parent? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun update(obj: Parent): Parent? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun delete(id: Id<Parent>): Parent? {
        return this.transaction {
            table.deleteReturning {
                table.id eq id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 

    public override suspend fun select(): List<Parent> {
        return this.transaction {
            table.selectAll().map(::toDomain)
        }
    }

    public override suspend fun select(id: Id<Parent>): Parent? {
        return this.transaction {
            table.selectAll()
                .where { table.id eq id.value }
                .singleOrNull()?.let(::toDomain)
        }
    }
}