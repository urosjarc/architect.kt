package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.Album
import kotlin.Int
import kotlin.String
import com.urosjarc.architect.test.core.domain.Genre
import com.urosjarc.architect.test.core.domain.MediaType
import kotlin.Float


@Serializable
public data class TrackNew(
    val albumId: Id<Album>,
    val bytes: Int,
    val composer: String,
    val genreId: Id<Genre>,
    val mediaTypeId: Id<MediaType>,
    val miliseconds: Int,
    val name: String,
    val unitPrice: Float,
)