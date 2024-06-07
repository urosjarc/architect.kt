package com.urosjarc.architect.lib.impl

import Architect
import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassDataNode
import com.urosjarc.architect.lib.domain.AState
import java.io.File
import java.util.*

public class RawDependencyObjectGenerator(
    private val appFile: File,
) : Generator {

    override fun generate(aState: AState) {
        var text = ""

        //TODO: Make this nested!!!
        Architect.getOrderedDependencies(aState = aState).forEach { aClassDataNode ->
            val kclass = aClassDataNode.aClassData.aClass.name
            val varle = kclass.replaceFirstChar { it.lowercase(Locale.getDefault()) }
            text += "lateinit var $varle: $kclass\n"
        }

        println(text)
    }

}
