package com.apollo9921.quizrise.domain.util

import android.app.Activity
import android.preference.PreferenceManager
import android.util.Log
import com.apollo9921.quizrise.BuildConfig
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics.ConsentStatus
import com.google.firebase.analytics.FirebaseAnalytics.ConsentType

class ConsentManager(private val activity: Activity) {

    private val consentInformation: ConsentInformation =
        UserMessagingPlatform.getConsentInformation(activity)

    fun isConsentGranted(): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val purposeConsents = prefs.getString("IABTCF_PurposeConsents", "") ?: ""
        return purposeConsents.isNotEmpty() && purposeConsents[0] == '1'
    }

    fun gatherConsent(
        testDeviceHashedId: String? = null,
        onConsentGathered: (canRequestAds: Boolean) -> Unit
    ) {
        val paramsBuilder = ConsentRequestParameters.Builder()
            .setTagForUnderAgeOfConsent(false)

        if (BuildConfig.DEBUG || !testDeviceHashedId.isNullOrEmpty()) {
            val debugSettingsBuilder = ConsentDebugSettings.Builder(activity)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)

            if (!testDeviceHashedId.isNullOrEmpty()) {
                debugSettingsBuilder.addTestDeviceHashedId(testDeviceHashedId)
            }

            paramsBuilder.setConsentDebugSettings(debugSettingsBuilder.build())
        }

        val params = paramsBuilder.build()

        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                    if (formError != null) {
                        Log.e("UMP_LOG", "Error to show form: ${formError.message}")
                    }

                    updateFirebaseConsent(isConsentGranted())
                    onConsentGathered(consentInformation.canRequestAds())
                }
            },
            { requestConsentError ->
                updateFirebaseConsent(isConsentGranted())
                onConsentGathered(consentInformation.canRequestAds())
                Log.e("UMP_LOG", "Error updating consent: ${requestConsentError.message}")
            }
        )
    }

    fun isPrivacyOptionsRequired(): Boolean {
        return consentInformation.privacyOptionsRequirementStatus ==
                ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED
    }

    fun showPrivacyOptionsForm(onDismiss: (formError: FormError?) -> Unit = {}) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
            if (formError != null) {
                Log.e("UMP_LOG", "Error reopen privacy options: ${formError.message}")
            }
            updateFirebaseConsent(isConsentGranted())
            onDismiss(formError)
        }
    }

    private fun updateFirebaseConsent(granted: Boolean) {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(activity)
        val status = if (granted) ConsentStatus.GRANTED else ConsentStatus.DENIED

        val consentMap = mapOf(
            ConsentType.ANALYTICS_STORAGE to status,
            ConsentType.AD_STORAGE to status,
            ConsentType.AD_USER_DATA to status,
            ConsentType.AD_PERSONALIZATION to status
        )

        firebaseAnalytics.setConsent(consentMap)
    }
}