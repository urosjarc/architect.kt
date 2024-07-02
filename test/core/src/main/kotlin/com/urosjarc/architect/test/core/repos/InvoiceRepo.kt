package com.urosjarc.architect.test.core.repos

import com.urosjarc.architect.test.core.domain.Invoice
import com.urosjarc.architect.test.core.models.InvoiceNew
import com.urosjarc.architect.test.core.models.InvoiceMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface InvoiceRepo : Repo<Invoice, InvoiceNew, InvoiceMod> {
}