package com.urosjarc.architect.lib.extend


internal val String.afterLastDot get(): String = this.split("<").first().split(".").last()
internal val String.beforeLastDot
    get(): String {
        val arr = this.split("<").first().split(".")
        return arr.subList(0, arr.size - 1).joinToString(".")
    }
