package com.urosjarc.architect.example.services

import com.urosjarc.architect.annotations.Service

@Service
public interface NetworkService {
    public fun emailDomainExist(email: String): Boolean
}
