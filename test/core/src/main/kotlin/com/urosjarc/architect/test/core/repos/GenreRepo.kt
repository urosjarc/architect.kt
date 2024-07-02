package com.urosjarc.architect.test.core.repos

import com.urosjarc.architect.test.core.domain.Genre
import com.urosjarc.architect.test.core.models.GenreNew
import com.urosjarc.architect.test.core.models.GenreMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface GenreRepo : Repo<Genre, GenreNew, GenreMod> {
}