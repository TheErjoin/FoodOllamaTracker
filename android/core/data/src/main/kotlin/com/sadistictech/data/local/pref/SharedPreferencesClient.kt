package com.sadistictech.data.local.pref

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesClient @Inject constructor(
    @ApplicationContext context: Context
) {

    val preferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    val editor = preferences.edit()
}