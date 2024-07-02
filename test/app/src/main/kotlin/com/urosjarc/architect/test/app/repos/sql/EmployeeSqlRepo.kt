package com.urosjarc.architect.test.app.repos.sql

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.test.app.repos.sql.EmployeeSql

public class EmployeeSqlRepo(db: Database) : EmployeeSql(db=db) {
}