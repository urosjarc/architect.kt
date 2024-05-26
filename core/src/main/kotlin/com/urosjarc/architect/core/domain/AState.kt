package com.urosjarc.architect.core.domain

import com.urosjarc.architect.DomainEntity
import com.urosjarc.architect.core.types.Id
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AState(
    public val id: Id<AState> = Id(),
    public val created: Instant = Clock.System.now(),
    public val domainEntities: List<AEntity>,
    public val repos: List<AEntity>,
    public val services: List<AEntity>,
    public val useCases: List<AEntity>,
)
