package com.urosjarc.architect.test.app.repos.sql

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.test.app.repos.sql.PlaylistSql

public class PlaylistSqlRepo(db: Database) : PlaylistSql(db=db) {
}