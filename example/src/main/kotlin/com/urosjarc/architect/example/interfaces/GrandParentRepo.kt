package com.urosjarc.architect.example.interfaces

import com.urosjarc.architect.example.domain.GrandParent
import com.urosjarc.architect.example.models.GrandParentNew
import com.urosjarc.architect.example.models.GrandParentMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface GrandParentRepo : Repo<GrandParent, GrandParentNew, GrandParentMod> {
}