package com.urosjarc.architect.lib.generators

import com.urosjarc.architect.lib.Architect
import java.io.File
import kotlin.test.Test


class Test_DomainModelsGenerator {

    @Test
    fun `test generate`() {
        val aState = Architect.getStateData(classPackages = Utils.classPackages)

        val domainModelsGen = DomainModelsGenerator(
            modelFolder = File("/home/urosjarc/vcs/architect.kt/lib/src/test/kotlin/com/urosjarc/architect.kt/lib/test_application/output/models"),
        )

        domainModelsGen.generate(aStateData = aState)

    }

}
