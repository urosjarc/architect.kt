package com.urosjarc.architect.test.app.repos.sql

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.test.app.repos.sql.InvoiceSql

public class InvoiceSqlRepo(db: Database) : InvoiceSql(db=db) {
}