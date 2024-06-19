package com.urosjarc.architect.lib.data

public data class FolderNode(
    var folder: String? = null,
    val level: Int,
    val children: MutableList<FolderNode> = mutableListOf(),
    val aClassDatas: MutableList<AClassData> = mutableListOf()
)
