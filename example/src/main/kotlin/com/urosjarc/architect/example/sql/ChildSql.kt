package com.urosjarc.architect.example.sql

import com.urosjarc.architect.example.interfaces.Repo
import com.urosjarc.architect.example.models.ChildNew
import com.urosjarc.architect.example.models.ChildMod
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers
import com.urosjarc.architect.example.domain.Child
import kotlin.String
import kotlinx.datetime.Instant
import com.urosjarc.architect.example.domain.Sex
import com.urosjarc.architect.example.types.Id
import kotlin.Double

public abstract class ChildSql(private val db: Database) : Repo<Child, ChildNew, ChildMod> {
    object table : UUIDTable(name = "Child", columnName = "id") {
        val name = varchar("name", 200)
        val email = varchar("email", 200)
        val surname = varchar("surname", 200)
        val birth = varchar("birth", 200)
        val sex = varchar("sex", 200)
        val parent = reference("parent", ParentSql.table)
        val cash = double("cash")
        val telephone = varchar("telephone", 200)
    }
    
    private fun toDomain(row: ResultRow) = Child(
        name = row[table.name],
        email = row[table.email],
        surname = row[table.surname],
        birth = Instant.parse(row[table.birth]),
        sex = Sex.valueOf(row[table.sex]),
        parent = Id(row[table.parent].value),
        cash = row[table.cash],
        telephone = row[table.telephone],
        id = Id(row[table.id].value),
    )
   
   internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }
    
    public override suspend fun insert(obj: ChildNew): Child? {
        return this.transaction {
            table.insert {
                it[name] = obj.name
                it[email] = obj.email
                it[surname] = obj.surname
                it[birth] = obj.birth.toString()
                it[sex] = obj.sex.toString()
            }.resultedValues?.singleOrNull()?.let(::toDomain)
        }
    }
    
    public override suspend fun insert(objs: Iterable<ChildNew>): List<Child> {
        val t = table
        return this.transaction {
            t.batchInsert(objs) {
                this[t.name] = it.name
                this[t.email] = it.email
                this[t.surname] = it.surname
                this[t.birth] = it.birth.toString()
                this[t.sex] = it.sex.toString()
            }.map(::toDomain)
        }
    }
    
    public override suspend fun updateMod(obj: ChildMod): Child? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[surname] = obj.surname
                it[parent] = obj.parent.value
                it[cash] = obj.cash
                it[telephone] = obj.telephone
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun update(obj: Child): Child? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[surname] = obj.surname
                it[parent] = obj.parent.value
                it[cash] = obj.cash
                it[telephone] = obj.telephone
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun delete(id: Id<Child>): Child? {
        return this.transaction {
            table.deleteReturning {
                table.id eq id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 

    public override suspend fun select(): List<Child> {
        return this.transaction {
            table.selectAll().map(::toDomain)
        }
    }

    public override suspend fun select(id: Id<Child>): Child? {
        return this.transaction {
            table.selectAll()
                .where { table.id eq id.value }
                .singleOrNull()?.let(::toDomain)
        }
    }
}