package com.urosjarc.architect.test.core.repos

import com.urosjarc.architect.test.core.domain.PlaylistTrack
import com.urosjarc.architect.test.core.models.PlaylistTrackNew
import com.urosjarc.architect.test.core.models.PlaylistTrackMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface PlaylistTrackRepo : Repo<PlaylistTrack, PlaylistTrackNew, PlaylistTrackMod> {
}