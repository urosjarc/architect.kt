package com.urosjarc.architect.test.app.repos.sql

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.test.app.repos.sql.GenreSql

public class GenreSqlRepo(db: Database) : GenreSql(db=db) {
}