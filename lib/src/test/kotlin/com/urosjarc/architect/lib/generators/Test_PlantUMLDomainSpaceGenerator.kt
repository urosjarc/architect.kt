package com.urosjarc.architect.lib.generators

import com.urosjarc.architect.lib.Architect
import java.io.File
import kotlin.test.Test


class Test_PlantUMLDomainSpaceGenerator {

    @Test
    fun `test generate`() {
        val aState = Architect.getStateData(classPackages = Utils.classPackages)

        val di = PlantUMLDomainSpaceGenerator(
            outputFile = File("/home/urosjarc/vcs/architect.kt/lib/build/output.plantuml"),
        )

        di.generate(aStateData = aState)

    }

}
