package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.MediaType


@Serializable
public data class MediaTypeMod(
    val id: Id<MediaType>,
)