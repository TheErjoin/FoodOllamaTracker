package com.sadistictech.home.domain.usecase

import com.sadistictech.domain.RemoteWrapper
import com.sadistictech.home.domain.model.StepsEstimate
import com.sadistictech.home.domain.model.StepsEstimateRequest
import com.sadistictech.home.domain.repository.HomeRepository
import javax.inject.Inject

class EstimateStepsUseCase @Inject constructor(
    private val repository: HomeRepository,
) {
    operator fun invoke(request: StepsEstimateRequest): RemoteWrapper<StepsEstimate> =
        repository.estimateSteps(request)
}
