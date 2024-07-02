package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.MediaType
import kotlin.String


@Serializable
public data class MediaTypeNew(
    val mediaTypeId: Id<MediaType>,
    val name: String,
)