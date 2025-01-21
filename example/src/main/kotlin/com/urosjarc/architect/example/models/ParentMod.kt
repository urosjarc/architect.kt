package com.urosjarc.architect.example.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.example.types.Id
import com.urosjarc.architect.example.domain.Parent


@Serializable
public data class ParentMod(
    val id: Id<Parent>,
)