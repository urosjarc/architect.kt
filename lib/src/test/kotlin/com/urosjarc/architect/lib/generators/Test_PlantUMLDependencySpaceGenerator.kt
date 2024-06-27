package com.urosjarc.architect.lib.generators

import com.urosjarc.architect.lib.Architect
import java.io.File
import kotlin.test.Test


class Test_PlantUMLDependencySpaceGenerator {

    @Test
    fun `test generate`() {
        val aState = Architect.getStateData("com.urosjarc.architect.lib.test_application")

        val di = PlantUMLDependencySpaceGenerator(
            outputFile = File("/home/urosjarc/vcs/architect/lib/src/test/kotlin/com/urosjarc/architect/lib/test_application/output/dependencies.plantuml"),
        )

        di.generate(aStateData = aState)

    }

}
