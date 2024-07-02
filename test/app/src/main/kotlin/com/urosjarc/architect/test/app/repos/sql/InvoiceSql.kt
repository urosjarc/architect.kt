package com.urosjarc.architect.test.app.repos.sql

import com.urosjarc.architect.test.core.repos.Repo
import com.urosjarc.architect.test.core.models.InvoiceNew
import com.urosjarc.architect.test.core.models.InvoiceMod
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers
import com.urosjarc.architect.test.core.domain.Invoice
import kotlin.String
import com.urosjarc.architect.test.core.types.Id
import kotlinx.datetime.Instant
import kotlin.Float

public abstract class InvoiceSql(private val db: Database) : Repo<Invoice, InvoiceNew, InvoiceMod> {
    object table : UUIDTable(name = "Invoice", columnName = "id") {
        val billingAddress = varchar("billingAddress", 200)
        val billingCity = varchar("billingCity", 200)
        val billingCountry = varchar("billingCountry", 200)
        val billingPostalCode = varchar("billingPostalCode", 200)
        val billingState = varchar("billingState", 200)
        val customerId = reference("customerId", CustomerSql.table)
        val invoiceDate = instant("invoiceDate")
        val total = float("total")
    }
    
    private fun toDomain(row: ResultRow) = Invoice(
        billingAddress = row[table.billingAddress],
        billingCity = row[table.billingCity],
        billingCountry = row[table.billingCountry],
        billingPostalCode = row[table.billingPostalCode],
        billingState = row[table.billingState],
        customerId = Id(row[table.customerId].value),
        id = Id(row[table.id].value),
        invoiceDate = row[table.invoiceDate],
        total = row[table.total],
    )
   
   internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }
    
    public override suspend fun insert(obj: InvoiceNew): Invoice? {
        return this.transaction {
            table.insert {
                it[billingAddress] = obj.billingAddress
                it[billingCity] = obj.billingCity
                it[billingCountry] = obj.billingCountry
                it[billingPostalCode] = obj.billingPostalCode
                it[billingState] = obj.billingState
                it[customerId] = obj.customerId.value
                it[invoiceDate] = obj.invoiceDate
                it[total] = obj.total
            }.resultedValues?.singleOrNull()?.let(::toDomain)
        }
    }
    
    public override suspend fun insert(objs: Iterable<InvoiceNew>): List<Invoice> {
        val t = table
        return this.transaction {
            t.batchInsert(objs) {
                this[t.billingAddress] = it.billingAddress
                this[t.billingCity] = it.billingCity
                this[t.billingCountry] = it.billingCountry
                this[t.billingPostalCode] = it.billingPostalCode
                this[t.billingState] = it.billingState
                this[t.customerId] = it.customerId.value
                this[t.invoiceDate] = it.invoiceDate
                this[t.total] = it.total
            }.map(::toDomain)
        }
    }
    
    public override suspend fun updateMod(obj: InvoiceMod): Invoice? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun update(obj: Invoice): Invoice? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun delete(id: Id<Invoice>): Invoice? {
        return this.transaction {
            table.deleteReturning {
                table.id eq id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 

    public override suspend fun select(): List<Invoice> {
        return this.transaction {
            table.selectAll().map(::toDomain)
        }
    }

    public override suspend fun select(id: Id<Invoice>): Invoice? {
        return this.transaction {
            table.selectAll()
                .where { table.id eq id.value }
                .singleOrNull()?.let(::toDomain)
        }
    }
}