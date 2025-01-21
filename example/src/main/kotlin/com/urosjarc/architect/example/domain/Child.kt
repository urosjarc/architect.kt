package com.urosjarc.architect.example.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.annotations.Mod
import com.urosjarc.architect.annotations.New
import com.urosjarc.architect.example.types.Id
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
@DomainEntity
public data class Child(
    @New val name: String,
    @New val email: String,
    @New @Mod val surname: String,
    @New val birth: Instant,
    @New val sex: Sex = Sex.UNDEFINED,

    @Mod val parent: Id<Parent>,
    @Mod val cash: Double,
    @Mod val telephone: String,

    val id: Id<Child> = Id()
)
