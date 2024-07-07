package com.urosjarc.architect.test.core.usecases

import com.urosjarc.architect.annotations.UseCase
import com.urosjarc.architect.test.core.repos.*
import com.urosjarc.architect.test.core.services.JwtService
import com.urosjarc.architect.test.core.services.PasswordService

@UseCase
public class Usecase4(
    private val artistRepo: ArtistRepo,
    private val invoiceLineRepo: InvoiceLineRepo,
    private val jwtService: JwtService
) {
    public fun now() {

    }
}
