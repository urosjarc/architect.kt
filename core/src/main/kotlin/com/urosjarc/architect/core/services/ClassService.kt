package com.urosjarc.architect.core.services

import com.urosjarc.architect.Service
import com.urosjarc.architect.core.domain.AState

@Service
public interface ClassService {
    public fun getState(packagePath: String): AState
}
