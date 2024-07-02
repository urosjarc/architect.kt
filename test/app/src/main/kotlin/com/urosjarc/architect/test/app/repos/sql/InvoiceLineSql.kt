package com.urosjarc.architect.test.app.repos.sql

import com.urosjarc.architect.test.core.repos.Repo
import com.urosjarc.architect.test.core.models.InvoiceLineNew
import com.urosjarc.architect.test.core.models.InvoiceLineMod
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers
import com.urosjarc.architect.test.core.domain.InvoiceLine
import com.urosjarc.architect.test.core.types.Id
import kotlin.Int
import kotlin.Float

public abstract class InvoiceLineSql(private val db: Database) : Repo<InvoiceLine, InvoiceLineNew, InvoiceLineMod> {
    object table : UUIDTable(name = "InvoiceLine", columnName = "id") {
        val customerId = reference("customerId", CustomerSql.table)
        val quantity = integer("quantity")
        val trackId = reference("trackId", TrackSql.table)
        val unitPrice = float("unitPrice")
    }
    
    private fun toDomain(row: ResultRow) = InvoiceLine(
        customerId = Id(row[table.customerId].value),
        id = Id(row[table.id].value),
        quantity = row[table.quantity],
        trackId = Id(row[table.trackId].value),
        unitPrice = row[table.unitPrice],
    )
   
   internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }
    
    public override suspend fun insert(obj: InvoiceLineNew): InvoiceLine? {
        return this.transaction {
            table.insert {
                it[customerId] = obj.customerId.value
                it[quantity] = obj.quantity
                it[trackId] = obj.trackId.value
                it[unitPrice] = obj.unitPrice
            }.resultedValues?.singleOrNull()?.let(::toDomain)
        }
    }
    
    public override suspend fun insert(objs: Iterable<InvoiceLineNew>): List<InvoiceLine> {
        val t = table
        return this.transaction {
            t.batchInsert(objs) {
                this[t.customerId] = it.customerId.value
                this[t.quantity] = it.quantity
                this[t.trackId] = it.trackId.value
                this[t.unitPrice] = it.unitPrice
            }.map(::toDomain)
        }
    }
    
    public override suspend fun updateMod(obj: InvoiceLineMod): InvoiceLine? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun update(obj: InvoiceLine): InvoiceLine? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun delete(id: Id<InvoiceLine>): InvoiceLine? {
        return this.transaction {
            table.deleteReturning {
                table.id eq id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 

    public override suspend fun select(): List<InvoiceLine> {
        return this.transaction {
            table.selectAll().map(::toDomain)
        }
    }

    public override suspend fun select(id: Id<InvoiceLine>): InvoiceLine? {
        return this.transaction {
            table.selectAll()
                .where { table.id eq id.value }
                .singleOrNull()?.let(::toDomain)
        }
    }
}