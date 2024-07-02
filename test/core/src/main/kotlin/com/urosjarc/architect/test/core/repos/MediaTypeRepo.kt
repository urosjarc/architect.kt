package com.urosjarc.architect.test.core.repos

import com.urosjarc.architect.test.core.domain.MediaType
import com.urosjarc.architect.test.core.models.MediaTypeNew
import com.urosjarc.architect.test.core.models.MediaTypeMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface MediaTypeRepo : Repo<MediaType, MediaTypeNew, MediaTypeMod> {
}