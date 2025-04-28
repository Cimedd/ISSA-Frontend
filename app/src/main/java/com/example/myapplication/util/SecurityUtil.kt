package com.example.myapplication.util

import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import android.util.Base64
import android.util.Log
import com.example.myapplication.dataclass.TransactionDetail
import com.google.gson.Gson


object SecurityUtil {

    private const val AES_MODE = "AES/GCM/NoPadding"
    private val gson = Gson()

    fun encrypt(plainText: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance(AES_MODE)

        // Automatically generates a random IV for AES GCM
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        // Encrypt the data
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        // Get the IV that was generated during encryption
        val iv = cipher.iv
        Log.d("IV ENC", Base64.encodeToString(iv, Base64.DEFAULT))

        // Combine the IV and encrypted data
        val combined = iv + encryptedBytes

        // Encode the combined data to Base64 for easier transmission/storage
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    fun decrypt(encryptedData: String, secretKey: SecretKey): String {
        val decoded = Base64.decode(encryptedData, Base64.DEFAULT)

        // Extract the IV from the first part of the decoded data (first 16 bytes for AES GCM)
        val iv = decoded.copyOfRange(0, 12)
        Log.d("IV DEC", Base64.encodeToString(iv, Base64.DEFAULT))

        // Extract the actual cipher text (the rest of the data)
        val cipherText = decoded.copyOfRange(12, decoded.size)

        // Initialize the cipher with AES/GCM mode
        val cipher = Cipher.getInstance(AES_MODE)

        // Set up the cipher with the IV and the secret key
        val spec = GCMParameterSpec(128, iv) // 128-bit authentication tag length
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

        // Decrypt the data
        val decryptedBytes = cipher.doFinal(cipherText)

        // Convert the decrypted bytes to a String and return it
        return String(decryptedBytes, Charsets.UTF_8)
    }

    fun sha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))

        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun encryptTransaction(transactionDetail: TransactionDetail): String {
        val json = gson.toJson(transactionDetail)
        return encrypt(json, KeyGenerator.getSecretKey())
    }

    fun decryptTransaction(encryptedData: String): TransactionDetail {
        val decryptedJson = decrypt(encryptedData, KeyGenerator.getSecretKey())
        return gson.fromJson(decryptedJson, TransactionDetail::class.java)
    }

}
