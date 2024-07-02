package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.Employee


@Serializable
public data class EmployeeMod(
    val id: Id<Employee>,
)