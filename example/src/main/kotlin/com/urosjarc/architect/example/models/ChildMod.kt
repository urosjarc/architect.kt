package com.urosjarc.architect.example.models

import kotlinx.serialization.Serializable 
import kotlin.String
import com.urosjarc.architect.example.types.Id
import com.urosjarc.architect.example.domain.Parent
import kotlin.Double
import com.urosjarc.architect.example.domain.Child


@Serializable
public data class ChildMod(
    val surname: String,
    val parent: Id<Parent>,
    val cash: Double,
    val telephone: String,
    val id: Id<Child>,
)