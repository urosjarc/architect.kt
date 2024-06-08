package com.urosjarc.architect.lib.data

public data class AClassDataNode(
    val aClassData: AClassData,
    val dependencies: MutableList<AClassDataNode> = mutableListOf(),
    val folders: MutableList<String>,
    var active: Boolean = true
) {
    override fun toString(): String {
        return aClassData.aClass.packagePath
    }
}
