package com.urosjarc.architect.test.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.test.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class Album(
    val id: Id<Album> = Id(),

    val artistId: Id<Artist>,
    val name: String,
) {

    private fun registerArtist() {

    }

    internal fun registerName(name: String) {

    }

    public fun withReturnType(): String = this.name
    public fun withReturnType(id: Id<Album>, id2: Id<Artist>): Id<Album> = Id()
}
