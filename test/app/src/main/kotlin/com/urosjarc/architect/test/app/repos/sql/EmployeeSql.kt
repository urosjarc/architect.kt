package com.urosjarc.architect.test.app.repos.sql

import com.urosjarc.architect.test.core.repos.Repo
import com.urosjarc.architect.test.core.models.EmployeeNew
import com.urosjarc.architect.test.core.models.EmployeeMod
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import kotlinx.coroutines.Dispatchers
import com.urosjarc.architect.test.core.domain.Employee
import kotlin.String
import kotlinx.datetime.Instant
import com.urosjarc.architect.test.core.types.Id

public abstract class EmployeeSql(private val db: Database) : Repo<Employee, EmployeeNew, EmployeeMod> {
    object table : UUIDTable(name = "Employee", columnName = "id") {
        val address = varchar("address", 200)
        val birthDate = instant("birthDate")
        val city = varchar("city", 200)
        val country = varchar("country", 200)
        val email = varchar("email", 200)
        val fax = varchar("fax", 200)
        val firstName = varchar("firstName", 200)
        val hireDate = instant("hireDate")
        val lastName = varchar("lastName", 200)
        val phone = varchar("phone", 200)
        val postalCode = varchar("postalCode", 200)
        val reportsTo = reference("reportsTo", EmployeeSql.table)
        val reportsToManager = reference("reportsToManager", EmployeeSql.table)
        val state = varchar("state", 200)
        val title = varchar("title", 200)
    }
    
    private fun toDomain(row: ResultRow) = Employee(
        address = row[table.address],
        birthDate = row[table.birthDate],
        city = row[table.city],
        country = row[table.country],
        email = row[table.email],
        fax = row[table.fax],
        firstName = row[table.firstName],
        hireDate = row[table.hireDate],
        id = Id(row[table.id].value),
        lastName = row[table.lastName],
        phone = row[table.phone],
        postalCode = row[table.postalCode],
        reportsTo = Id(row[table.reportsTo].value),
        reportsToManager = Id(row[table.reportsToManager].value),
        state = row[table.state],
        title = row[table.title],
    )
   
   internal suspend fun <T> transaction(block: suspend (t: Transaction) -> T): T = newSuspendedTransaction(Dispatchers.IO, db) { block(this) }
    
    public override suspend fun insert(obj: EmployeeNew): Employee? {
        return this.transaction {
            table.insert {
                it[address] = obj.address
                it[birthDate] = obj.birthDate
                it[city] = obj.city
                it[country] = obj.country
                it[email] = obj.email
                it[fax] = obj.fax
                it[firstName] = obj.firstName
                it[hireDate] = obj.hireDate
                it[lastName] = obj.lastName
                it[phone] = obj.phone
                it[postalCode] = obj.postalCode
                it[reportsTo] = obj.reportsTo.value
                it[reportsToManager] = obj.reportsToManager.value
                it[state] = obj.state
                it[title] = obj.title
            }.resultedValues?.singleOrNull()?.let(::toDomain)
        }
    }
    
    public override suspend fun insert(objs: Iterable<EmployeeNew>): List<Employee> {
        val t = table
        return this.transaction {
            t.batchInsert(objs) {
                this[t.address] = it.address
                this[t.birthDate] = it.birthDate
                this[t.city] = it.city
                this[t.country] = it.country
                this[t.email] = it.email
                this[t.fax] = it.fax
                this[t.firstName] = it.firstName
                this[t.hireDate] = it.hireDate
                this[t.lastName] = it.lastName
                this[t.phone] = it.phone
                this[t.postalCode] = it.postalCode
                this[t.reportsTo] = it.reportsTo.value
                this[t.reportsToManager] = it.reportsToManager.value
                this[t.state] = it.state
                this[t.title] = it.title
            }.map(::toDomain)
        }
    }
    
    public override suspend fun updateMod(obj: EmployeeMod): Employee? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun update(obj: Employee): Employee? {
        return this.transaction {
            table.updateReturning(where = { table.id eq obj.id.value }) { 
                it[id] = obj.id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 
    
    public override suspend fun delete(id: Id<Employee>): Employee? {
        return this.transaction {
            table.deleteReturning {
                table.id eq id.value
            }.singleOrNull()?.let(::toDomain)
        }
    } 

    public override suspend fun select(): List<Employee> {
        return this.transaction {
            table.selectAll().map(::toDomain)
        }
    }

    public override suspend fun select(id: Id<Employee>): Employee? {
        return this.transaction {
            table.selectAll()
                .where { table.id eq id.value }
                .singleOrNull()?.let(::toDomain)
        }
    }
}