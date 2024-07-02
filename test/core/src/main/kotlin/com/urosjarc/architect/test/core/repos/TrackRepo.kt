package com.urosjarc.architect.test.core.repos

import com.urosjarc.architect.test.core.domain.Track
import com.urosjarc.architect.test.core.models.TrackNew
import com.urosjarc.architect.test.core.models.TrackMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface TrackRepo : Repo<Track, TrackNew, TrackMod> {
}