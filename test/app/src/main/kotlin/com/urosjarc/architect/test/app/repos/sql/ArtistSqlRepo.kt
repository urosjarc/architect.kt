package com.urosjarc.architect.test.app.repos.sql

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.test.app.repos.sql.ArtistSql

public class ArtistSqlRepo(db: Database) : ArtistSql(db=db) {
}