package com.sadistictech.home.domain.usecase

import com.sadistictech.domain.RemoteWrapper
import com.sadistictech.home.domain.model.ActivityEstimate
import com.sadistictech.home.domain.model.ActivityEstimateRequest
import com.sadistictech.home.domain.repository.HomeRepository
import javax.inject.Inject

class EstimateActivityUseCase @Inject constructor(
    private val repository: HomeRepository,
) {
    operator fun invoke(request: ActivityEstimateRequest): RemoteWrapper<ActivityEstimate> =
        repository.estimateActivity(request)
}
