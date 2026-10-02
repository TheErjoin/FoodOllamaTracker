package com.sadistictech.data.utils

import com.sadistictech.foodollamatracker.data.BuildConfig
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECT_TIMEOUT_SECONDS: Long = 15
private const val REQUEST_TIMEOUT_SECONDS: Long = 150
private const val WRITE_TIMEOUT_SECONDS: Long = 30
private const val MEDIA_TYPE = "application/json; charset=UTF8"

/**
 * Builds and configures Retrofit instance.
 *
 * @see Retrofit
 */
internal fun buildRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(BuildConfig.BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(
        jsonClient.asConverterFactory(MEDIA_TYPE.toMediaType())
    )
    .build()

/**
 * Creates and configures an [OkHttpClient].[Builder][OkHttpClient.Builder] instance.
 */
internal fun createOkHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient()
    .newBuilder()
    .addInterceptor(provideLoggingInterceptor())
    // Local Ollama can take up to 120 seconds before the backend uses its fallback.
    .callTimeout(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
    .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
    .readTimeout(REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
    .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)

/**
 * Provides a logging interceptor based on the build configuration.
 */
private fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }
}


/**
 * Converts the response body to a specific API error type.
 *
 * @receiver [ResponseBody] - The response body.
 * @return [T] - The API error object.
 * @throws NullPointerException if the response body cannot be converted.
 */
internal inline fun <reified T> ResponseBody?.toApiError(): T {
    return this?.let { jsonClient.decodeFromString<T>(it.string()) } ?: throw NullPointerException(
        "JsonUtil cannot convert fromJson: ${T::class.java.simpleName}"
    )
}
