package com.urosjarc.architect.lib.generators

import com.urosjarc.architect.lib.Architect
import java.io.File
import kotlin.test.Test


class Test_JetbrainsExposedRepositoryGenerator {

    @Test
    fun `test generate`() {
        val aStateData = Architect.getStateData(classPackages = Utils.classPackages)

        val exposedRepoGen = JetbrainsExposedRepositoryGenerator(
            interfaceFolder = File("/home/urosjarc/vcs/architect.kt/lib/output/interfaces"),
            sqlFolder = File("/home/urosjarc/vcs/architect.kt/lib/output/sql"),
            repoFolder = File("/home/urosjarc/vcs/architect.kt/lib/output/repos"),
            modelFolder = File("/home/urosjarc/vcs/architect.kt/lib/output/models"),
            mapping = listOf(
                "com.urosjarc.architect.lib.types.Id" to JetbrainsExposedTypeMapping(
                    { "reference(\"${it.aProp.name}\", ${it.aTypeParams[0].name}Sql.table)" },
                    { "Id(row[table.${it.aProp.name}].value)" },
                    { ".value" },
                ),
                "kotlin.collections.List" to JetbrainsExposedTypeMapping(
                    { "blob(\"${it.aProp.name}\")" },
                    { "Json.deserialize(row[table.${it.aProp.name}].value)" },
                    { "" },
                ),
                "com.urosjarc.architect.lib.domain.AVisibility" to JetbrainsExposedTypeMapping(
                    { "varchar(\"${it.aProp.name}\", 200)" },
                    { "AVisibility.parse(row[table.${it.aProp.name}].value)" },
                    { "" },
                ),
                "kotlinx.datetime.Instant" to JetbrainsExposedTypeMapping(
                    { "varchar(\"${it.aProp.name}\", 200)" },
                    { "AVisibility.parse(row[table.${it.aProp.name}].value)" },
                    { "" },
                )
            ),
        )

        exposedRepoGen.generate(aStateData = aStateData)

    }

}
