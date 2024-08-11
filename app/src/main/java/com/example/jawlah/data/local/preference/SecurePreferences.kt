package com.example.jawlah.data.local.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class SecurePreferences(private val context: Context) : AppPreferences {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val encryptedSharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        BLINK_SECURE_PREFERENCES_FILE_NAME,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )


    companion object {
        private const val BLINK_SECURE_PREFERENCES_FILE_NAME = ""
    }
}

enum class AppLanguage {
    AR
}