package com.urosjarc.architect.core.use_cases

import com.urosjarc.architect.UseCase
import com.urosjarc.architect.core.domain.AState
import com.urosjarc.architect.core.services.ClassService

@UseCase
public class Scan_project_arhitecture(
    private val classService: ClassService
) {

    public sealed interface Result {
        public data class OK(val aState: AState) : Result

    }

    public fun now(packagePath: String): Result {
        return Result.OK(aState = this.classService.getState(packagePath = packagePath))
    }

}
