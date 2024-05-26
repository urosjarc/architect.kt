package com.urosjarc.architect.api

import com.urosjarc.architect.api.repos.sql.UserSqlRepo
import com.urosjarc.architect.api.services.ClassGraphService
import com.urosjarc.architect.core.repos.UserRepo
import com.urosjarc.architect.core.services.ClassService
import com.urosjarc.architect.core.services.JsonService
import com.urosjarc.architect.core.use_cases.Scan_project_arhitecture
import com.urosjarc.architect.core.use_cases.Seed_repos
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

public object App {
    // REPOS
    public lateinit var userRepo: UserRepo

    // SERVICES
    public lateinit var jsonService: JsonService
    public lateinit var classService: ClassService

    // USE CASES
    public lateinit var scan_project_arhitecture: Scan_project_arhitecture
    public lateinit var seed_repos: Seed_repos

    public fun init(seed: Boolean = true) {

        /** REPOS */
        initSqlRepos(seed = seed)

        /** SERVICES */
        jsonService = JsonService(prettyPrint = true)
        classService = ClassGraphService()

        /** USE CAESES */
        scan_project_arhitecture = Scan_project_arhitecture(classService = this.classService)
        seed_repos = Seed_repos(userRepo = this.userRepo)

        check()

        if (seed) seed_repos.now()
    }

    private fun initSqlRepos(seed: Boolean) {
        val db =
            Database.connect(
                HikariDataSource(
                    HikariConfig().apply {
                        this.password = Env.DB_PASSWORD
                        this.username = Env.DB_USERNAME
                        this.jdbcUrl = Env.DB_URL
                        this.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
                        this.validate()
                    },
                ),
            )

        userRepo = UserSqlRepo(db = db)

        if (seed) {
            transaction(db) {
                SchemaUtils.drop(UserSqlRepo.table)
                SchemaUtils.create(UserSqlRepo.table)
            }
        }
    }

    private fun check() {
        // TODO: Implement me!
    }
}
