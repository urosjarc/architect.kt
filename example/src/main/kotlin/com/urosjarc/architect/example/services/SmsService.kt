package com.urosjarc.architect.example.services

import com.urosjarc.architect.annotations.Service

@Service
public interface SmsService {
    public fun send(text: String): Boolean
}
