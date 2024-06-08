package com.urosjarc.architect.lib.impl

import Architect
import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.FolderNode
import com.urosjarc.architect.lib.domain.AState
import java.io.File
import java.util.*

public class RawDependencyObjectGenerator(
    private val appFile: File,
) : Generator {


    override fun generate(aState: AState) {
        val rootFolder = Architect.getFolderNodes(aState = aState)
        rootFolder.folder = "App"
        val lines = mutableListOf<String>()
        this.recursion(node = rootFolder, lines)
        appFile.writeText(lines.joinToString("\n"))
    }

    private fun recursion(node: FolderNode, lines: MutableList<String>) {
        lines.add("    ".repeat(node.level) + "public object ${node.folder} {")
        node.aClassDatas.forEach {
            lines.add("    ".repeat(node.level + 1) + "lateinit var ${it.aClass.name.replaceFirstChar { it.lowercase(Locale.getDefault()) }}: ${it.aClass.name}")
        }
        node.children.forEach { this.recursion(node = it, lines = lines) }
        lines.add("    ".repeat(node.level) + "}")
    }

}
