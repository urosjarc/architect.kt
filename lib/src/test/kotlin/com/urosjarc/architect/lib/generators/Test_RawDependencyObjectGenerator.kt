package com.urosjarc.architect.lib.generators

import com.urosjarc.architect.lib.Architect
import java.io.File
import kotlin.test.Test


class Test_RawDependencyObjectGenerator {

    @Test
    fun `test generate`() {
        val aState = Architect.getStateData(classPackages = Utils.classPackages, "com.urosjarc.architect.lib.test_application")

        val di = RawDependencyObjectGenerator(
            appFile = File("/home/urosjarc/vcs/architect/lib/src/test/kotlin/com/urosjarc/architect.kt/lib/test_application/output/App.kt"),
            startMark = "//START MARK",
            endMark = "//END MARK"
        )

//        di.generate(aState = aState)

    }

}
