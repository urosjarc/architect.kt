package com.urosjarc.architect.test.core.repos

import com.urosjarc.architect.test.core.domain.Customer
import com.urosjarc.architect.test.core.models.CustomerNew
import com.urosjarc.architect.test.core.models.CustomerMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface CustomerRepo : Repo<Customer, CustomerNew, CustomerMod> {
}