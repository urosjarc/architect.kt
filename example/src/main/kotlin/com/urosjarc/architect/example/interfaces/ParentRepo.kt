package com.urosjarc.architect.example.interfaces

import com.urosjarc.architect.example.domain.Parent
import com.urosjarc.architect.example.models.ParentNew
import com.urosjarc.architect.example.models.ParentMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface ParentRepo : Repo<Parent, ParentNew, ParentMod> {
}