package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.Artist
import kotlin.String


@Serializable
public data class AlbumNew(
    val artistId: Id<Artist>,
    val name: String,
)