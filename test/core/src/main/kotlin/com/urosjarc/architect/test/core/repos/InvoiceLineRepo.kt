package com.urosjarc.architect.test.core.repos

import com.urosjarc.architect.test.core.domain.InvoiceLine
import com.urosjarc.architect.test.core.models.InvoiceLineNew
import com.urosjarc.architect.test.core.models.InvoiceLineMod
import com.urosjarc.architect.annotations.Repository

@Repository
public interface InvoiceLineRepo : Repo<InvoiceLine, InvoiceLineNew, InvoiceLineMod> {
}