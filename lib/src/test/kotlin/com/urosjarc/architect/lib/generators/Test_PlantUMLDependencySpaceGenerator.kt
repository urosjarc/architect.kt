package com.urosjarc.architect.lib.generators

import Architect
import java.io.File
import kotlin.test.Test


class Test_PlantUMLDependencySpaceGenerator {

    @Test
    fun `test generate`() {
        val aState = Architect.getState("com.urosjarc.architect.lib.test_application")

        val di = PlantUMLDependencySpaceGenerator(
            outputFile = File("/home/urosjarc/vcs/architect/lib/src/test/kotlin/com/urosjarc/architect/lib/test_application/output/dependencies.plantuml"),
        )

//        di.generate(aState = aState)

    }

}
