package com.sadistictech.home.data.repository

import com.sadistictech.data.base.BaseRepository
import com.sadistictech.home.data.model.EstimateActivityRequestData
import com.sadistictech.home.data.model.EstimateCaloriesRequestData
import com.sadistictech.home.data.model.EstimateStepsRequestData
import com.sadistictech.home.data.network.HomeApiService
import com.sadistictech.home.domain.model.ActivityEstimateRequest
import com.sadistictech.home.domain.model.FoodEstimateRequest
import com.sadistictech.home.domain.model.StepsEstimateRequest
import com.sadistictech.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val apiService: HomeApiService,
) : BaseRepository(), HomeRepository {

    override fun estimateFood(request: FoodEstimateRequest) = networkFlow {
        apiService.estimateCalories(request = EstimateCaloriesRequestData(description = request.description))
    }

    override fun estimateActivity(request: ActivityEstimateRequest) = networkFlow {
        apiService.estimateActivity(
            request = EstimateActivityRequestData(
                description = request.description,
                weightKg = request.weightKg,
                durationMinutes = request.durationMinutes
            )
        )
    }

    override fun estimateSteps(request: StepsEstimateRequest) = networkFlow {
        apiService.estimateSteps(
            request = EstimateStepsRequestData(
                steps = request.steps,
                weightKg = request.weightKg,
                heightCm = request.heightCm
            )
        )
    }
}
