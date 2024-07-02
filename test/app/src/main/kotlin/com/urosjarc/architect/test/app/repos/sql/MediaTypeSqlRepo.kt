package com.urosjarc.architect.test.app.repos.sql

import org.jetbrains.exposed.sql.Database
import com.urosjarc.architect.test.app.repos.sql.MediaTypeSql

public class MediaTypeSqlRepo(db: Database) : MediaTypeSql(db=db) {
}