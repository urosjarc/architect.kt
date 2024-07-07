package com.urosjarc.architect.test.core.services

import com.urosjarc.architect.annotations.Service

@Service
public interface PasswordService {
	public fun generate(): String
}
