package com.urosjarc.architect.core.services

import com.urosjarc.architect.Service
import com.urosjarc.architect.core.serializers.BytesSerializer
import com.urosjarc.architect.core.serializers.UUIDSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

@Service
public class JsonService(prettyPrint: Boolean = false) {
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
