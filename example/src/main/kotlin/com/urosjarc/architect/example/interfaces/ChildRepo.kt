package com.urosjarc.architect.example.interfaces

import com.urosjarc.architect.example.domain.Child
import com.urosjarc.architect.example.models.ChildNew
import com.urosjarc.architect.example.models.ChildMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface ChildRepo : Repo<Child, ChildNew, ChildMod> {
}