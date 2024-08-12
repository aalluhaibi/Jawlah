package com.example.jawlah.data.local.realm

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.security.SecureRandom

class RealmSecurePreferences(context: Context) : RealmPreferences {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val encryptedSharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        REALM_SECURE_PREFERENCES_FILE_NAME,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun getRealmKey(): ByteArray {
        val savedKey: String = getSecureValueFromPrefs()
        return if (savedKey.isBlank()) {
            val key = generateKey()
            val keyString = encodeToString(key)
            setSecureKeyToPrefs(keyString)
            key
        } else {
            decodeFromString(savedKey)
        }
    }


    private fun setSecureKeyToPrefs(value: String) = encryptedSharedPreferences.edit {
        putString(REALM_KEY, value)
    }

    private fun getSecureValueFromPrefs(): String = encryptedSharedPreferences.getString(
        REALM_KEY, ""
    ) ?: ""

    private fun encodeToString(aKey: ByteArray): String {
        return Base64.encodeToString(aKey, Base64.DEFAULT)
    }

    private fun decodeFromString(aSavedKey: String): ByteArray {
        return Base64.decode(aSavedKey, Base64.DEFAULT)
    }

    private fun generateKey(): ByteArray {
        val key = ByteArray(64)
        SecureRandom().nextBytes(key)
        return key
    }


    companion object {
        private const val REALM_SECURE_PREFERENCES_FILE_NAME = "REALM_SECURE_PREFERENCES_FILE_NAME"
        private const val REALM_KEY = "RealmSec"
    }
}