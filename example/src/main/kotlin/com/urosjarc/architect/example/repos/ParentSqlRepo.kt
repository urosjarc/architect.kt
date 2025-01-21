package com.urosjarc.architect.example.repos

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.example.sql.ParentSql

public class ParentSqlRepo(db: Database) : ParentSql(db=db) {
}