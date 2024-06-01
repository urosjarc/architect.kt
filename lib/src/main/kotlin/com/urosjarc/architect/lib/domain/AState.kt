package com.urosjarc.architect.lib.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.lib.data.AClassData
import com.urosjarc.architect.lib.types.Id
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class AState(
    public val identifiers: List<AClassData>,
    public val domainEntities: List<AClassData>,
    public val repos: List<AClassData>,
    public val services: List<AClassData>,
    public val useCases: List<AClassData>,

    public val id: Id<AState> = Id(),
    public val created: Instant = Clock.System.now(),
)
