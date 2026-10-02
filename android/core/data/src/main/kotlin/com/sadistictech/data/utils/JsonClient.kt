package com.sadistictech.data.utils

import android.content.Context
import kotlinx.serialization.json.Json

internal val jsonClient = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

/**
 * Get Json from [assets][getAssets]
 *
 * @return Json file from [assets][getAssets]
 */
internal fun Context.jsonFromAssets(fileName: String): String {
    return this.assets.open(fileName).bufferedReader().use { it.readText() }
}
