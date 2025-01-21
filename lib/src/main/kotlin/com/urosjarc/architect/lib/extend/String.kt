package com.urosjarc.architect.lib.extend


public val String.ext_capitalized: String get() = this.replaceFirstChar { it.uppercase() }
public val String.ext_afterLastDot: String get() = this.split("<").first().split(".").last()
public val String.ext_firstLine: String get() = this.split("\n").first()
public val String.ext_beforeLastDot: String
    get(): String {
        val arr = this.split("<").first().split(".")
        return arr.subList(0, arr.size - 1).joinToString(".")
    }
