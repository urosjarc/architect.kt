package com.urosjarc.architect.test.app.repos.sql

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.test.app.repos.sql.PlaylistTrackSql

public class PlaylistTrackSqlRepo(db: Database) : PlaylistTrackSql(db=db) {
}