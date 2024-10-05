package com.urosjarc.architect.test.core.usecases

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.test.core.repos.CustomerRepo
import com.urosjarc.architect.test.core.repos.EmployeeRepo
import com.urosjarc.architect.test.core.repos.MediaTypeRepo
import com.urosjarc.architect.test.core.repos.TrackRepo
import com.urosjarc.architect.test.core.services.JsonService
import com.urosjarc.architect.test.core.services.PasswordService

@UseCase
public class Usecase3(
    private val jsonService: JsonService,
    private val usecase4: Usecase4,
    private val usecase2: Usecase2,
    private val trackRepo: TrackRepo,
    private val mediaTypeRepo: MediaTypeRepo,
    private val employeeRepo: EmployeeRepo,
    private val customerRepo: CustomerRepo,
    private val passwordService: PasswordService
) {
    public fun now(): String {
        return "asdf"

    }
}
