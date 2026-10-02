package com.sadistictech.data.base

import android.util.Log
import com.sadistictech.data.utils.DataMapper
import com.sadistictech.data.utils.toApiError
import com.sadistictech.domain.Either
import com.sadistictech.domain.NetworkError
import com.sadistictech.domain.RemoteWrapper
import com.sadistictech.foodollamatracker.data.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.io.InterruptedIOException

abstract class BaseRepository {
    // Need to extract to DI
    private val ioDispatcher = Dispatchers.IO

    protected fun <T : DataMapper<S>, S> networkFlow(
        request: suspend () -> Response<T>
    ): RemoteWrapper<S> = doNetworkRequest(request) { responseBody ->
        responseBody.asDomain()
    }

    private fun <T, S> doNetworkRequest(
        request: suspend () -> Response<T>,
        successful: (T) -> S
    ) = flow {
        val response = request()
        when {
            response.isSuccessful -> {
                val body = response.body()

                if (body != null) {
                    emit(Either.Right(successful.invoke(body)))
                } else {
                    emit(Either.Left(NetworkError.Unexpected("Body is null WTF?")))
                }
            }

            !response.isSuccessful && response.code() == 422 -> {
                emit(Either.Left(NetworkError.ApiInputs(response.errorBody().toApiError())))
            }

            else -> {
                emit(Either.Left(NetworkError.Api(response.errorBody().toApiError())))
            }
        }
    }.catch { exception ->
        when (exception) {
            is InterruptedIOException -> {
                emit(Either.Left(NetworkError.Timeout))
            }

            else -> {
                val message = exception.localizedMessage ?: "Unexpected Error! (check BaseRepo)"
                if (BuildConfig.DEBUG) Log.e("BaseRepository", message)
                emit(Either.Left(NetworkError.Unexpected(message)))
            }
        }
    }.flowOn(ioDispatcher)
}