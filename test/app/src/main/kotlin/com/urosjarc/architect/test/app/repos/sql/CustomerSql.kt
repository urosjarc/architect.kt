package com.urosjarc.architect.test.app.repos.sql

import com.urosjarc.architect.test.core.domain.Customer
import com.urosjarc.architect.test.core.models.CustomerMod
import com.urosjarc.architect.test.core.models.CustomerNew
import com.urosjarc.architect.test.core.repos.Repo
import com.urosjarc.architect.test.core.types.Id
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

public abstract class CustomerSql(private val db: Database) : Repo<Customer, CustomerNew, CustomerMod> {
    object table : UUIDTable(name = "Customer", columnName = "id") {
        val address = varchar("address", 200)
        val city = varchar("city", 200)
        val company = varchar("company", 200)
        val country = varchar("country", 200)
        val email = varchar("email", 200)
        val fax = varchar("fax", 200)
        val firstName = varchar("firstName", 200)
        val lastName = varchar("lastName", 200)
        val phone = varchar("phone", 200)
        val postalCode = varchar("postalCode", 200)
        val state = varchar("state", 200)
        val supportRepId = reference("supportRepId", EmployeeSql.table)
    }

    private fun toDomain(row: ResultRow) = Customer(
        address = row[table.address],
        city = row[table.city],
        company = row[table.company],
        country = row[table.country],
        email = row[table.email],
        fax = row[table.fax],
        firstName = row[table.firstName],
        id = Id(row[table.id].value),
        lastName = row[table.lastName],
        phone = row[table.phone],
        postalCode = row[table.postalCode],
        state = row[table.state],
        supportRepId = Id(row[table.supportRepId].value),
    )

    internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }

    public override suspend fun insert(obj: CustomerNew): Customer? {
        return this.transaction {
            table.insert {
                it[address] = obj.address
                it[city] = obj.city
                it[company] = obj.company
                it[country] = obj.country
                it[email] = obj.email
                it[fax] = obj.fax
                it[phone] = obj.phone
                it[postalCode] = obj.postalCode
                it[state] = obj.state
                it[supportRepId] = obj.supportRepId.value
            }.resultedValues?.singleOrNull()?.let(::toDomain)
        }
    }

    public override suspend fun insert(objs: Iterable<CustomerNew>): List<Customer> {
        val t = table
        return this.transaction {
            t.batchInsert(objs) {
                this[t.address] = it.address
                this[t.city] = it.city
                this[t.company] = it.company
                this[t.country] = it.country
                this[t.email] = it.email
                this[t.fax] = it.fax
                this[t.phone] = it.phone
                this[t.postalCode] = it.postalCode
                this[t.state] = it.state
                this[t.supportRepId] = it.supportRepId.value
            }.map(::toDomain)
        }
    }

    public override suspend fun updateMod(obj: CustomerMod): Customer? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) {
                it[address] = obj.address
                it[city] = obj.city
                it[company] = obj.company
                it[firstName] = obj.firstName
                it[id] = obj.id.value
                it[lastName] = obj.lastName
                it[state] = obj.state
            }.singleOrNull()?.let(::toDomain)
        }
    }

    public override suspend fun update(obj: Customer): Customer? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) {
                it[address] = obj.address
                it[city] = obj.city
                it[company] = obj.company
                it[firstName] = obj.firstName
                it[id] = obj.id.value
                it[lastName] = obj.lastName
                it[state] = obj.state
            }.singleOrNull()?.let(::toDomain)
        }
    }

    public override suspend fun delete(id: Id<Customer>): Customer? {
        return this.transaction {
            table.deleteReturning {
                table.id eq id.value
            }.singleOrNull()?.let(::toDomain)
        }
    }

    public override suspend fun select(): List<Customer> {
        return this.transaction {
            table.selectAll().map(::toDomain)
        }
    }

    public override suspend fun select(id: Id<Customer>): Customer? {
        return this.transaction {
            table.selectAll()
                .where { table.id eq id.value }
                .singleOrNull()?.let(::toDomain)
        }
    }
}
