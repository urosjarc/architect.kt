package com.urosjarc.architect.lib.impl

import Architect
import java.io.File
import kotlin.test.Test


class Test_RawDependencyObjectGenerator {

    @Test
    fun `test generate`() {
        val aState = Architect.getState("com.urosjarc.architect.lib.test_application")

        val di = RawDependencyObjectGenerator(
            appFile = File("/home/urosjarc/vcs/architect/lib/src/test/kotlin/com/urosjarc/architect/lib/test_application/output/App.kt"),
        )

        di.generate(aState = aState)

    }

}
