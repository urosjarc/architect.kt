package com.urosjarc.architect.lib.impl

import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.domain.AState
import java.io.File

public class RawDependencyInjectionGenerator(
    private val appFile: File,
) : Generator {

    private val dependencies: MutableMap<AClassData, List<AClassData>> = mutableMapOf()

    override fun generate(aState: AState) {

        aState.useCases.forEach { it: AClassData ->

            this.dependencies[it] = listOf()
        }

    }

}
