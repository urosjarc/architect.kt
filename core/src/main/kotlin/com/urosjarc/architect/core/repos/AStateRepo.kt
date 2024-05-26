package com.urosjarc.architect.core.repos

import com.urosjarc.architect.annotations.Repository
import com.urosjarc.architect.core.domain.AState

@Repository<AState>
public interface AStateRepo : Repo<AState> {


}
