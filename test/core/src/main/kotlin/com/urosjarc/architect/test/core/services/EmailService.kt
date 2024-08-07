package com.urosjarc.architect.test.core.services

import com.urosjarc.architect.annotations.Service
import com.urosjarc.architect.test.core.types.Encrypted


@Service
public interface EmailService {
    public fun exists(email: Encrypted): Boolean

    public fun send(from: Encrypted, to: Encrypted, sender: String, subject: String, html: String): Boolean

}
