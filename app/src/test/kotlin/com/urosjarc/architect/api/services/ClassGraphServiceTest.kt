package com.urosjarc.architect.api.services

import com.urosjarc.architect.core.services.JsonService
import kotlin.test.Test
import kotlin.test.assertTrue

class ClassGraphServiceTest {
    @Test
    fun `test get state`() {
        val graphService = ClassGraphService()
        val state = graphService.getState(packagePath = "com.urosjarc.architect.core")
        val json = JsonService(prettyPrint = true)
        println(json.encode(value = state))
        assertTrue(true)
    }
}
