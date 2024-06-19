package com.urosjarc.architect.lib.test_application.services

import com.urosjarc.architect.annotations.Service

@Service
class JsonService(prettyPrint: Boolean = false) {
    inline fun <reified T> encode(value: T): String {
        TODO()
    }

    inline fun <reified T> decode(value: String): T {
        TODO()
    }
}
