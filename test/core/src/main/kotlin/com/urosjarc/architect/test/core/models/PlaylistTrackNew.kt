package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.Playlist
import com.urosjarc.architect.test.core.domain.Track


@Serializable
public data class PlaylistTrackNew(
    val playlistId: Id<Playlist>,
    val trackId: Id<Track>,
)