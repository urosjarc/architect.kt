package com.urosjarc.architect.test.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.test.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class MediaType(
    val id: Id<MediaType> = Id(),
    val mediaTypeId: Id<MediaType>,

    val name: String,
)
