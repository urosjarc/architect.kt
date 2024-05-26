package com.urosjarc.architect.api.services

import kotlin.test.Test
import kotlin.test.assertTrue

class ClassGraphServiceTest {
    @Test
    fun `test get state`() {
        val graphService = ClassGraphService()
        val state = graphService.getState(packagePath = "com.urosjarc.architect.core")
        println(state)
        assertTrue(true)
    }
}
