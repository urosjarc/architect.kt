package com.urosjarc.architect.lib.impl

import Architect
import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.domain.AState
import java.io.File
import java.util.*

public class RawDependencyObjectGenerator(
    private val appFile: File,
) : Generator {



    override fun generate(aState: AState) {
        var text = ""

        val rootFolder = Architect.getFolderNodes(aState = aState)

        println(text)
    }

}
