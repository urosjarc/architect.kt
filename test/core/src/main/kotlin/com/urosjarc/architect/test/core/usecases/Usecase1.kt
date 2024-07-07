package com.urosjarc.architect.test.core.usecases

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.test.core.repos.InvoiceRepo
import com.urosjarc.architect.test.core.services.EmailService

@UseCase
public class Usecase1(
    private val emailService: EmailService,
    private val invoiceRepo: InvoiceRepo,
    private val usecase0: Usecase0
) {
    public fun now() {

    }
}
