package com.urosjarc.architect.test.app.repos.sql

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.test.app.repos.sql.TrackSql

public class TrackSqlRepo(db: Database) : TrackSql(db=db) {
}