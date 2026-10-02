package com.sadistictech.data.local.pref

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataPreferences @Inject constructor(
    preferencesClient: SharedPreferencesClient,
) {
    private val preferences = preferencesClient.preferences
    private val editor = preferencesClient.editor

    var isOnboardingComplete: Boolean
        get() = preferences.getBoolean(PreferencesConstants.PREF_IS_ONBOARDING, false)
        set(value) = editor.putBoolean(PreferencesConstants.PREF_IS_ONBOARDING, value).apply()
}