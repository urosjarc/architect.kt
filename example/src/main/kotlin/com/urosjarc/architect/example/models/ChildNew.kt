package com.urosjarc.architect.example.models

import kotlinx.serialization.Serializable 
import kotlin.String
import kotlinx.datetime.Instant
import com.urosjarc.architect.example.domain.Sex


@Serializable
public data class ChildNew(
    val name: String,
    val email: String,
    val surname: String,
    val birth: Instant,
    val sex: Sex,
)