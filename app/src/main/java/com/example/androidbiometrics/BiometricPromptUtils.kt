package com.example.androidbiometrics

import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

object BiometricPromptUtils {
    fun createBiometricPrompt(
        activity: AppCompatActivity,
        processSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        processFail: () -> Unit,
        processErrorOrUsePIN: (Int) -> Unit
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                processSuccess(result)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                processFail()
            }

            /**
             * errCode: 7
             * errString: "Too many attempts. Try again later."
             *
             * errCode: 13
             * errString: "Use PIN" this is our setNegativeButtonText from BiometricPrompt
             */
            override fun onAuthenticationError(errCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errCode, errString)
                processErrorOrUsePIN(errCode)
            }
        }
        return BiometricPrompt(activity, executor, callback)
    }

    // TODO study about setDeviceCredentialAllowed(true)
    fun createPromptInfo(activity: AppCompatActivity): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder().apply {
            setTitle(activity.getString(R.string.prompt_info_title))
            setSubtitle(activity.getString(R.string.prompt_info_subtitle))
            setDescription(activity.getString(R.string.prompt_info_description))
            setConfirmationRequired(false)
            setNegativeButtonText(activity.getString(R.string.prompt_info_use_app_pin))
        }.build()
}