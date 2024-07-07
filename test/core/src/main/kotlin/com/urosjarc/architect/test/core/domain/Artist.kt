package com.urosjarc.architect.test.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.annotations.Var
import com.urosjarc.architect.test.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class Artist(
    val id: Id<Artist> = Id(),

    @Var val name: String,
)
