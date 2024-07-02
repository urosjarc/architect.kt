package com.urosjarc.architect.test.core.repos

import com.urosjarc.architect.test.core.domain.Artist
import com.urosjarc.architect.test.core.models.ArtistNew
import com.urosjarc.architect.test.core.models.ArtistMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface ArtistRepo : Repo<Artist, ArtistNew, ArtistMod> {
}