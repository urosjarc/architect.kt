package com.urosjarc.architect.lib.impl

import Architect
import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassDataNode
import com.urosjarc.architect.lib.data.FolderNode
import com.urosjarc.architect.lib.domain.AState
import java.io.File
import java.util.*

public class RawDependencyObjectGenerator(
    private val appFile: File,
    private val startMark: String,
    private val endMark: String
) : Generator {


    override fun generate(aState: AState) {
        val rootFolder = Architect.getFolderNodes(aState = aState)
        val injection = this.injection(aState = aState)
        val lines = mutableListOf<String>()
        val check = mutableListOf<String>()
        this.recursion(node = rootFolder, lines = lines, check = check)

        val newLines = mutableListOf<String>()
        var ignore = false
        for (line in appFile.readLines()) {
            if(line.contains(startMark)) {
                ignore = true
                newLines.add(line)
                newLines.addAll(lines)
                continue
            }
            if(line.contains(endMark)) {
                ignore = false
                newLines.add("    public fun initDependencies(){")
                newLines.addAll(injection)
                newLines.add("    }")
                newLines.add("    public fun checkDependencies(){")
                newLines.addAll(check)
                newLines.add("    }")
                newLines.add(line)
                continue
            }
            if(!ignore) newLines.add(line)
        }
        appFile.writeText(newLines.joinToString("\n"))
    }

    private fun recursion(node: FolderNode, lines: MutableList<String>, check: MutableList<String>) {
        if (node.folder != null) lines.add("    ".repeat(node.level) + "public object ${node.folder!!.capitalize()} {")
        node.aClassDatas.forEach {
            val variable = it.aClass.name.replaceFirstChar { it.lowercase(Locale.getDefault()) }
            lines.add("    ".repeat(node.level + 1) + "lateinit var ${variable}: ${it.aClass.name}")
            check.add("    ".repeat(2) + "logger.info(${variable})")
        }
        node.children.forEach { this.recursion(node = it, lines = lines, check = check) }
        if (node.folder != null) lines.add("    ".repeat(node.level) + "}")
    }
    public fun injection(aState: AState): MutableList<String> {
        val lines = mutableListOf<String>()

        Architect.getOrderedDependencies(aState = aState).forEach { aClassDataNode ->
            val kclass = aClassDataNode.aClassData.aClass.name
            val varle = kclass.replaceFirstChar { it.lowercase(Locale.getDefault()) }
            lines.add("    ".repeat(2) + "$varle = $kclass(")

            val args = mutableListOf<String>()
            aClassDataNode.dependencies.forEach { di: AClassDataNode ->
                val di_varle = di.aClassData.aClass.name.replaceFirstChar { it.lowercase(Locale.getDefault()) }
                args.add("    ".repeat(3) + "$di_varle = $di_varle,")
            }

            if(args.isNotEmpty()) {
                lines.addAll(args)
                lines.add("    ".repeat(2) + ")")
            } else {
                val lastLine = lines.removeLast()
                lines.add("$lastLine)")
            }
        }

        return lines
    }
}
