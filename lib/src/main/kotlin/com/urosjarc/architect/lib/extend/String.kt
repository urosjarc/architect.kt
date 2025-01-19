package com.urosjarc.architect.lib.extend


public val String.afterLastDot: String get() = this.split("<").first().split(".").last()
public val String.beforeLastDot: String
    get(): String {
        val arr = this.split("<").first().split(".")
        return arr.subList(0, arr.size - 1).joinToString(".")
    }
