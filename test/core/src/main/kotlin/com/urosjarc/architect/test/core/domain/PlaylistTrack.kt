package com.urosjarc.architect.test.core.domain

import com.urosjarc.architect.annotations.DomainEntity
import com.urosjarc.architect.test.core.types.Id
import kotlinx.serialization.Serializable

@DomainEntity
@Serializable
public data class PlaylistTrack(
    val id: Id<PlaylistTrack> = Id(),
    val trackId: Id<Track>,
    val playlistId: Id<Playlist>,
)
