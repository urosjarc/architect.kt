package com.urosjarc.architect.example.repos

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.example.sql.ChildSql

public class ChildSqlRepo(db: Database) : ChildSql(db=db) {
}