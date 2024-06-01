package com.urosjarc.architect.lib.impl

import Architect
import java.io.File
import kotlin.test.Test


class Test_JetbrainsExposedRepositoryGenerator {

    @Test
    fun `test generate`() {
        val aState = Architect.getState("com.urosjarc.architect.lib.test_application")

        val exposedRepoGen = JetbrainsExposedRepositoryGenerator(
            interfaceFolder = File("/home/urosjarc/vcs/architect/lib/src/test/kotlin/com/urosjarc/architect/lib/test_application/output/interfaces"),
            sqlFolder = File("/home/urosjarc/vcs/architect/lib/src/test/kotlin/com/urosjarc/architect/lib/test_application/output/sql"),
            repoFolder = File("/home/urosjarc/vcs/architect/lib/src/test/kotlin/com/urosjarc/architect/lib/test_application/output/repos"),
            mapping = listOf(
                "kotlin.String" to Triple(
                    { "varchar(\"${it.aProp.name}\", 200)" },
                    { "row[table.${it.aProp.name}]" },
                    { "" },
                ),
                "com.urosjarc.architect.lib.test_application.domain.UId" to Triple(
                    { "reference(\"${it.aProp.name}\", ${it.aTypeParams[0].name}Sql.table)" },
                    { "UId(row[table.${it.aProp.name}].value)" },
                    { ".value" },
                ),
                "com.urosjarc.architect.lib.test_application.domain.Id" to Triple(
                    { "reference(\"${it.aProp.name}\", ${it.aTypeParams[0].name}Sql.table)" },
                    { "Id(row[table.${it.aProp.name}].value)" },
                    { ".value" },
                ),
                "com.urosjarc.architect.lib.test_application.domain.User.Type" to Triple(
                    { "varchar(\"${it.aProp.name}\", 200)" },
                    { "User.Type.valueOf(row[table.${it.aProp.name}])" },
                    { ".name" },
                )
            ),
        )

        exposedRepoGen.generate(aState = aState)

    }

}
