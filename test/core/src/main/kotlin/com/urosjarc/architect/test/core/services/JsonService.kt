package com.urosjarc.architect.test.core.services

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import com.urosjarc.architect.test.core.serializers.BytesSerializer
import shared.core.serializers.UUIDSerializer

public open class JsonService(prettyPrint: Boolean = false) {
	public val module: Json =
		Json {
			serializersModule =
				SerializersModule {
					contextual(UUIDSerializer)
					contextual(BytesSerializer)
				}
			this.prettyPrint = prettyPrint
			this.isLenient = true
			this.allowSpecialFloatingPointValues = true
		}

	public inline fun <reified T> encode(value: T): String {
		return this.module.encodeToString(value)
	}

	public inline fun <reified T> decode(value: String): T {
		return this.module.decodeFromString<T>(value)
	}
}
