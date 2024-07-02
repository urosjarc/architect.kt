package com.urosjarc.architect.test.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.test.core.types.Id
import kotlinx.serialization.Serializable


@DomainEntity
@Serializable
public data class Track(
    val id: Id<Track> = Id(),
    val albumId: Id<Album>,
    val mediaTypeId: Id<MediaType>,
    val genreId: Id<Genre>,

    val name: String,
    val composer: String,
    val miliseconds: Int,
    val bytes: Int,
    val unitPrice: Float,
)
