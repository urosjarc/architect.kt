package com.urosjarc.architect.example.repos

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.example.sql.GrandParentSql

public class GrandParentSqlRepo(db: Database) : GrandParentSql(db=db) {
}