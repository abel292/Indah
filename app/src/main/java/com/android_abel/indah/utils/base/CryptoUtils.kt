package com.android_abel.indah.utils.base

import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

object CryptoUtils {
    @JvmStatic
    fun getSecret(size: Int): String {
        val secret = getSecretBytes(size)
        return Base64.encodeBytes(secret)
    }

    fun getSecretBytes(size: Int): ByteArray {
        val secret = ByteArray(size)
        secureRandom.nextBytes(secret)
        return secret
    }

    val secureRandom: SecureRandom
        get() = try {
            SecureRandom.getInstance("SHA1PRNG")
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError(e)
        }

    fun generateRegistrationId(extendedRange: Boolean): Int {
        return try {
            val secureRandom =
                SecureRandom.getInstance("SHA1PRNG")
            if (extendedRange) secureRandom.nextInt(Int.MAX_VALUE - 1) + 1 else secureRandom.nextInt(
                16380
            ) + 1
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError(e)
        }
    }
}