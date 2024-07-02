package com.urosjarc.architect.test.app.repos.sql

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.test.app.repos.sql.InvoiceLineSql

public class InvoiceLineSqlRepo(db: Database) : InvoiceLineSql(db=db) {
}