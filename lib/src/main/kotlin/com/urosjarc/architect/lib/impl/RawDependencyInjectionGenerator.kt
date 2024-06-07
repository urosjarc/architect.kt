package com.urosjarc.architect.lib.impl

import Architect
import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassDataNode
import com.urosjarc.architect.lib.domain.AState
import java.io.File
import java.util.*

public class RawDependencyInjectionGenerator(
    private val appFile: File,
) : Generator {

    override fun generate(aState: AState) {
        var text = ""

        Architect.getOrderedDependencies(aState = aState).forEach { aClassDataNode ->
            val kclass = aClassDataNode.aClassData.aClass.name
            val varle = kclass.replaceFirstChar { it.lowercase(Locale.getDefault()) }
            text += "$varle = $kclass("

            val args = mutableListOf<String>()
            aClassDataNode.dependencies.forEach { di: AClassDataNode ->
                val di_varle = di.aClassData.aClass.name.replaceFirstChar { it.lowercase(Locale.getDefault()) }
                args.add("    $di_varle = $di_varle,")
            }

            if(args.isNotEmpty()) text += "\n"
            text += args.joinToString("\n")
            if(args.isNotEmpty()) text += "\n"
            text += ")\n"
        }

        println(text)
    }

}
