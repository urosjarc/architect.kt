package com.urosjarc.architect.generators

import com.urosjarc.architect.lib.Architect
import com.urosjarc.architect.lib.Generator
import com.urosjarc.architect.lib.data.AClassDataNode
import com.urosjarc.architect.lib.data.AStateData
import com.urosjarc.architect.lib.data.FolderNode
import java.io.File
import java.util.*

public class RawDependencyObjectGenerator(
    private val appFile: File,
    private val startMark: String,
    private val endMark: String
) : Generator {


    override fun generate(aStateData: AStateData) {
        val appFileExists = appFile.exists()
        val rootFolder = Architect.getFolderNodes(aStateData = aStateData)
        val injection = this.injection(aStateData = aStateData)
        val lines = mutableListOf<String>()
        val check = mutableListOf<String>()
        val deps = Architect.getOrderedDependencies(aStateData = aStateData)
        this.fillLinesAndCheck(node = rootFolder, lines = lines, check = check)

        if(!appFileExists) {
            appFile.createNewFile()
            val lines = deps.map { "import ${it.aClassData.aClass.import}" } + listOf(
                "public object App {",
                "    $startMark",
                "    $endMark",
                "}"
            )
            appFile.writeText(lines.joinToString("\n"))
        }

        val newLines = mutableListOf<String>()
        var ignore = false
        for (line in appFile.readLines()) {
            if (line.contains(startMark)) {
                ignore = true
                newLines.add(line)
                newLines.addAll(lines)
                continue
            }
            if (line.contains(endMark)) {
                ignore = false
                if(!appFileExists){
                    newLines.add("    public fun init(){")
                    newLines.addAll(injection)
                    newLines.add("    }")
                }
                newLines.add("    public fun check(){")
                newLines.addAll(check)
                newLines.add("    }")
                newLines.add(line)
                continue
            }
            if (!ignore) newLines.add(line)
        }
        appFile.writeText(newLines.joinToString("\n"))
    }

    private fun fillLinesAndCheck(node: FolderNode, lines: MutableList<String>, check: MutableList<String>) {
        if (node.folder != null) lines.add("    ".repeat(node.level) + "public object ${node.folder} {")
        node.aClassDatas.forEach {
            val variable = it.aClass.name.replaceFirstChar { it.lowercase(Locale.getDefault()) }
            lines.add("    ".repeat(node.level + 1) + "public lateinit var ${variable}: ${it.aClass.name}")
            check.add("    ".repeat(2) + "println(\"${it.aClass.name} -> \${${node.fullPath}.${variable}}\")")
        }
        node.children.forEach { this.fillLinesAndCheck(node = it, lines = lines, check = check) }
        if (node.folder != null) lines.add("    ".repeat(node.level) + "}")
    }

    public fun injection(aStateData: AStateData): MutableList<String> {
        val lines = mutableListOf<String>()

        Architect.getOrderedDependencies(aStateData = aStateData).forEach { aClassDataNode ->
            val kclass = aClassDataNode.aClassData.aClass.name
            val varle = kclass.replaceFirstChar { it.lowercase(Locale.getDefault()) }
            lines.add("    ".repeat(2) + "${aClassDataNode.fullPath}.$varle = $kclass(")

            val args = mutableListOf<String>()
            aClassDataNode.dependencies.forEach { di: AClassDataNode ->
                val di_varle = di.aClassData.aClass.name.replaceFirstChar { it.lowercase(Locale.getDefault()) }
                args.add("    ".repeat(3) + "${di.fullPath}.$di_varle = $di_varle,")
            }

            if (args.isNotEmpty()) {
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
