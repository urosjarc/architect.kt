package com.urosjarc.architect.test.core.repos

import com.urosjarc.architect.test.core.domain.Employee
import com.urosjarc.architect.test.core.models.EmployeeNew
import com.urosjarc.architect.test.core.models.EmployeeMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface EmployeeRepo : Repo<Employee, EmployeeNew, EmployeeMod> {
}