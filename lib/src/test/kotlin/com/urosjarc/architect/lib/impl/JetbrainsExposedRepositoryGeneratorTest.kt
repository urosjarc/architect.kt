package com.urosjarc.architect.lib.impl

import com.urosjarc.architect.lib.Architect
import java.io.File
import kotlin.test.Test


class JetbrainsExposedRepositoryGeneratorTest {

    @Test
    fun `test generate`(){
        val aState = Architect.getState("com.urosjarc.architect.lib.test_application")

        val exposedRepoGen = JetbrainsExposedRepositoryGenerator(
            interfaceFolder = File(this::class.java.getResource("/interfaces")!!.toURI()),
            implementationFolder = File(this::class.java.getResource("/impl")!!.toURI())
        )

        exposedRepoGen.generate(aState = aState)

    }

}
