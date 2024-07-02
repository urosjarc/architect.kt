package com.urosjarc.architect.test.app.repos.sql

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.test.app.repos.sql.AlbumSql

public class AlbumSqlRepo(db: Database) : AlbumSql(db=db) {
}