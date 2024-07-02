package com.urosjarc.architect.test.core.repos

import com.urosjarc.architect.test.core.domain.Playlist
import com.urosjarc.architect.test.core.models.PlaylistNew
import com.urosjarc.architect.test.core.models.PlaylistMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface PlaylistRepo : Repo<Playlist, PlaylistNew, PlaylistMod> {
}