package com.urosjarc.architect.lib.generators

import kotlinx.datetime.Instant
import kotlinx.serialization.json.Json
import java.util.*

object Utils {
    val classPackages = mapOf(
        "UUID" to "java.util",
        "String" to "kotlin",
        "Float" to "kotlin",
        "Int" to "kotlin",
        "Json" to "kotlinx.serialization.json",
        "Boolean" to "kotlin",
        "Instant" to "kotlinx.datetime"
    )
}

fun main() {
//    UUID.randomUUID()
//    String
//    Instant
    Json {  }
}
