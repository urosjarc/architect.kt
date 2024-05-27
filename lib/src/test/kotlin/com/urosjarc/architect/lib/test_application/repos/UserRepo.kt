package com.urosjarc.architect.lib.test_application.repos

import com.urosjarc.architect.annotations.Repository
import com.urosjarc.architect.lib.test_application.domain.User

@Repository<User>
interface UserRepo: Repo<User>
