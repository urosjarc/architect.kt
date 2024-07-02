package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.PlaylistTrack


@Serializable
public data class PlaylistTrackMod(
    val id: Id<PlaylistTrack>,
)