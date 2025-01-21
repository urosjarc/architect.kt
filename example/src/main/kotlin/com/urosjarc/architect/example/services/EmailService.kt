package com.urosjarc.architect.example.services

import com.urosjarc.architect.annotations.Service

/**
 * Provides functionality for sending emails.
 *
 * This service interface defines a contract for sending email messages. Implementations
 * of this interface are expected to handle the actual email transmission process.
 */
@Service
public interface EmailService {
    public fun send(email: String): Boolean
}
