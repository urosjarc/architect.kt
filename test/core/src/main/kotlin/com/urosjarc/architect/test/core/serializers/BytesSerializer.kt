package com.urosjarc.architect.test.core.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public object BytesSerializer : KSerializer<ByteArray> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("kotlin.Bytes", PrimitiveKind.STRING)

	override fun deserialize(decoder: Decoder): ByteArray {
		return decoder.decodeString().toByteArray(charset = Charsets.UTF_8)
	}

	override fun serialize(
		encoder: Encoder,
		value: ByteArray,
	) {
		encoder.encodeString(value.toString(charset = Charsets.UTF_8))
	}
}
