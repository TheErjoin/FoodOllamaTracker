package com.sadistictech.home.domain.usecase

import com.sadistictech.domain.RemoteWrapper
import com.sadistictech.home.domain.model.FoodEstimateRequest
import com.sadistictech.home.domain.model.NutritionEstimate
import com.sadistictech.home.domain.repository.HomeRepository
import javax.inject.Inject

class EstimateFoodUseCase @Inject constructor(
    private val repository: HomeRepository,
) {
    operator fun invoke(request: FoodEstimateRequest): RemoteWrapper<NutritionEstimate> =
        repository.estimateFood(request)
}
