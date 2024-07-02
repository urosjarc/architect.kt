package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.Genre


@Serializable
public data class GenreMod(
    val id: Id<Genre>,
)