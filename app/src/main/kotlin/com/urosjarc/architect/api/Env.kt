package com.urosjarc.architect.api

internal object Env {
	val DB_URL: String = System.getenv("DB_URL")
	val DB_USERNAME: String = System.getenv("DB_USERNAME")
	val DB_PASSWORD: String = System.getenv("DB_PASSWORD")
}
