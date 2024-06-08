package com.urosjarc.architect.lib.data

public data class FolderNode(
    val folder: String? = null,
    val children: MutableList<FolderNode> = mutableListOf(),
    val aClassDatas: MutableList<AClassData> = mutableListOf()
)
