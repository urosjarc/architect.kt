package com.urosjarc.architect.test.core.repos

import com.urosjarc.architect.test.core.domain.Album
import com.urosjarc.architect.test.core.models.AlbumNew
import com.urosjarc.architect.test.core.models.AlbumMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface AlbumRepo : Repo<Album, AlbumNew, AlbumMod> {
}