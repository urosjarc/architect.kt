package com.urosjarc.architect.lib.generators

import Architect
import java.io.File
import kotlin.test.Test


class Test_DomainModelsGenerator {

    @Test
    fun `test generate`() {
        val aState = Architect.getState("com.urosjarc.architect.lib.test_application")

        val domainModelsGen = DomainModelsGenerator(
            modelFolder = File("/home/urosjarc/vcs/architect/lib/src/test/kotlin/com/urosjarc/architect/lib/test_application/output/models"),
        )

//        domainModelsGen.generate(aState = aState)

    }

}
