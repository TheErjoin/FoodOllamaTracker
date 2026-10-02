package com.sadistictech.domain

import kotlinx.coroutines.flow.Flow

typealias RemoteWrapper<T> = Flow<Either<NetworkError, T>>