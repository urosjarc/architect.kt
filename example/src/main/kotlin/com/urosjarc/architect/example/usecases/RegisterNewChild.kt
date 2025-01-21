package com.urosjarc.architect.example.usecases

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.example.domain.Child
import com.urosjarc.architect.example.interfaces.ChildRepo
import com.urosjarc.architect.example.models.ChildNew
import com.urosjarc.architect.example.services.EmailService
import com.urosjarc.architect.example.services.NetworkService
import com.urosjarc.architect.example.services.SmsService

/**
 * Represents a use case for registering a new child in the system.
 *
 * This class handles the process of registering a child, which includes verifying
 * that the child does not already exist, checking that the provided email domain
 * is valid, saving the child into the repository, and sending notifications through email
 * and SMS upon successful registration.
 *
 * @property childRepo Repository for handling child data storage and retrieval operations.
 * @property emailService Service for sending email notifications.
 * @property smsService Service for sending SMS notifications.
 * @property networkService Service for validating email domain existence.
 */
@UseCase
public data class RegisterNewChild(
    val childRepo: ChildRepo,
    val emailService: EmailService,
    val smsService: SmsService,
    val networkService: NetworkService
) {

    public sealed interface Result {
        public data class OK(val child: Child, val emailSend: Boolean, val smsSend: Boolean) : Result
        public data object CHILD_ALREADY_EXISTS : Result
        public data class EMAIL_DOESNOT_EXISTS(val email: String) : Result
    }

    suspend fun now(childNew: ChildNew): Result {

        if (!this.networkService.emailDomainExist(email = childNew.name))
            return Result.EMAIL_DOESNOT_EXISTS(email = childNew.name)

        val child = this.childRepo.insert(obj = childNew)!! ?: return Result.CHILD_ALREADY_EXISTS

        val emailSend = this.emailService.send(email = "Registrirani ste...")
        val smsSend = this.smsService.send(text = "Registrirani ste...")

        return Result.OK(child = child, emailSend = emailSend, smsSend = smsSend)

    }
}
