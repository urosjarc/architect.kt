package com.urosjarc.architect.test.app.repos.sql

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.test.app.repos.sql.CustomerSql

public class CustomerSqlRepo(db: Database) : CustomerSql(db=db) {
}