package com.urosjarc.architect.lib.generators

import com.urosjarc.architect.lib.Architect
import java.io.File
import kotlin.test.Test


class Test_PlantUMLDomainSpaceGenerator {

    @Test
    fun `test generate`() {
        val aState = Architect.getStateData("com.urosjarc.architect.lib.test_application")

        val di = PlantUMLDomainSpaceGenerator(
            outputFile = File("/home/urosjarc/vcs/architect/lib/src/test/kotlin/com/urosjarc/architect/lib/test_application/output/domain.plantuml"),
        )

//        di.generate(aState = aState)

    }

}
