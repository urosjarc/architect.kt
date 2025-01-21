package com.urosjarc.architect.lib.data

public data class FolderNode(
    var folder: String? = null,
    val level: Int,
    val parent: FolderNode? = null,
    val children: MutableList<FolderNode> = mutableListOf(),
    val aClassDatas: MutableList<AClassData> = mutableListOf()
) {
    val fullPath: String
        get(): String {
            var path = this.folder ?: ""
            while(this.parent != null) this.parent.let { path += "/${it.folder}"}

            return path
        }
}
