package com.urosjarc.architect.test.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.test.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class Album(
    val id: Id<Album> = Id(),

    val artistId: Id<Artist>,
    val name: String,
)
