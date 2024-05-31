package com.urosjarc.architect.lib.impl

import com.urosjarc.architect.lib.Architect
import java.io.File
import kotlin.test.Test


class Test_JetbrainsExposedRepositoryGenerator {

    @Test
    fun `test generate`() {
        val aState = Architect.getState("com.urosjarc.architect.lib.test_application")

        val exposedRepoGen = JetbrainsExposedRepositoryGenerator(
            interfaceFolder = File("/home/urosjarc/vcs/architect/lib/src/test/kotlin/com/urosjarc/architect/lib/test_application/output/interfaces"),
            implementationFolder = File("/home/urosjarc/vcs/architect/lib/src/test/kotlin/com/urosjarc/architect/lib/test_application/output/impl"),
            mapping = listOf(
                "kotlin.String" to { "varchar(\"${it.name}\", 200)" },
                "com.urosjarc.architect.lib.test_application.domain.UId" to { "binary(\"${it.name}\")" },
                "com.urosjarc.architect.lib.test_application.domain.Id" to { "binary(\"${it.name}\")" },
                "com.urosjarc.architect.lib.test_application.domain.User.Type" to { "varchar(\"${it.name}\", 200)"}
            )
        )

        exposedRepoGen.generate(aState = aState)

    }

}
