package com.sadistictech.data.remote

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkClient @Inject constructor(
    private val retrofit: Retrofit,
) {

    fun <T : Any> createApiService(serviceClass: Class<T>): T = retrofit.create(serviceClass)
}
