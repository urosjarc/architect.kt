package com.urosjarc.architect.lib.data

import com.urosjarc.architect.lib.domain.AState
import kotlinx.serialization.Serializable

@Serializable
public data class AStateData(
    public val state: AState,
    public val identifiers: List<AClassData>,
    public val domainEntities: List<AClassData>,
    public val repos: List<AClassData>,
    public val services: List<AClassData>,
    public val useCases: List<AClassData>,
)
