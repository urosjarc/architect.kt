package com.urosjarc.architect.example.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.example.types.Id
import com.urosjarc.architect.example.domain.GrandParent


@Serializable
public data class GrandParentMod(
    val id: Id<GrandParent>,
)