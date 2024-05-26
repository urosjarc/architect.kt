package com.urosjarc.architect.core.domain

import com.urosjarc.architect.DomainEntity
import com.urosjarc.architect.core.data.AClassData
import com.urosjarc.architect.core.types.Id
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AState(
    public val created: Instant = Clock.System.now(),
    public val domainEntities: List<AClassData>,
    public val repos: List<AClassData>,
    public val services: List<AClassData>,
    public val useCases: List<AClassData>,
    public val id: Id<AState> = Id(),
)
