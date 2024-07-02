package com.urosjarc.architect.test.core.services

import kotlin.time.Duration

/**
 * Class for issuing / verifying
 * JWT refresh and auth tokens
 */
public interface JwtService {

	public sealed interface VerifyResult {
		public data class Valid(val data: String, val audience: String, val expiration: Duration) : VerifyResult
		public data class Invalid(val info: String) : VerifyResult

	}

	public fun generate(json: String, lifetime: Duration): String

	public fun verify(token: String): VerifyResult

}
