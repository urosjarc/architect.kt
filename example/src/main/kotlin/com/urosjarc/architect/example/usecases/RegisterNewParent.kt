package com.urosjarc.architect.example.usecases

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.example.domain.Child
import com.urosjarc.architect.example.interfaces.ParentRepo
import com.urosjarc.architect.example.models.ChildNew
import com.urosjarc.architect.example.services.EmailService
import com.urosjarc.architect.example.services.NetworkService

/**
 * Represents a use case for registering a new parent in the system.
 *
 * The `RegisterNewParent` class handles the process of adding a new parent, which involves
 * leveraging the `ParentRepo` for storing or checking parent data, making use of the
 * `RegisterNewChild` use case for managing child registration, and validating essential
 * network-related operations via the `NetworkService`.
 *
 * This class ensures integration between parent registration and its associated child registration
 * process, creating a structured way to handle data storage and cross-service communications.
 *
 * @property parentRepo Repository for accessing and storing parent-related data.
 * @property registerNewChild Use case for managing child registration.
 * @property networkService Service for handling network-related validations.
 */
@UseCase
public data class RegisterNewParent(
    val parentRepo: ParentRepo,
    val registerNewChild: RegisterNewChild,
    val networkService: NetworkService
) {

    public sealed interface Result {
        public data object OK : Result
    }

    public fun now(childNew: ChildNew): Result {

        return Result.OK

    }
}
