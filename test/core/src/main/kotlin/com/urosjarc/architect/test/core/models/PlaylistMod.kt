package com.urosjarc.architect.test.core.models

import kotlinx.serialization.Serializable 
import com.urosjarc.architect.test.core.types.Id
import com.urosjarc.architect.test.core.domain.Playlist


@Serializable
public data class PlaylistMod(
    val id: Id<Playlist>,
)