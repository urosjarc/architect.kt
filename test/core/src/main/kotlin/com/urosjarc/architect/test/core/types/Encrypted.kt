package com.urosjarc.architect.test.core.types

import com.google.crypto.tink.DeterministicAead
import com.google.crypto.tink.InsecureSecretKeyAccess
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.TinkJsonProtoKeysetFormat
import com.google.crypto.tink.daead.DeterministicAeadConfig
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

private val KEYSET_ENCRYPTED =
    """{"primaryKeyId":1742477480,"key":[{"keyData":{"typeUrl":"type.googleapis.com/google.crypto.tink.AesSivKey","value":"EkAFTLfbKnOV3XOOK3mRH9TOiP3ybYedaFUAbSEsHXrpp/qfbEZWZHK/dJKxzWnd6p8QoPoVkolt02wgjBTQpgn5","keyMaterialType":"SYMMETRIC"},"status":"ENABLED","keyId":1742477480,"outputPrefixType":"TINK"}]}"""
private var unit: Unit = DeterministicAeadConfig.register()
private var handle: KeysetHandle = TinkJsonProtoKeysetFormat.parseKeyset(KEYSET_ENCRYPTED, InsecureSecretKeyAccess.get())
private var daead: DeterministicAead = handle.getPrimitive(DeterministicAead::class.java)

@OptIn(ExperimentalEncodingApi::class)
public fun String.encrypted(): Encrypted {
    val ciphertext = daead.encryptDeterministically(this.encodeToByteArray(), byteArrayOf())
    return Encrypted(value = Base64.UrlSafe.encodeToByteArray(ciphertext))
}

@JvmInline
@Serializable
public value class Encrypted(
    @Contextual public val value: ByteArray,
) {
    override fun toString(): String = this.value.decodeToString()

    @OptIn(ExperimentalEncodingApi::class)
    public fun decrypt(): String {
        val ciphertext = Base64.UrlSafe.decode(this.value)
        return daead.decryptDeterministically(ciphertext, byteArrayOf()).decodeToString()
    }
}
