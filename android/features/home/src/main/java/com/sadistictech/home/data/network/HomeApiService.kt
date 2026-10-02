package com.sadistictech.home.data.network

import com.sadistictech.home.data.model.EstimateActivityDto
import com.sadistictech.home.data.model.EstimateActivityRequestData
import com.sadistictech.home.data.model.EstimateCaloriesDto
import com.sadistictech.home.data.model.EstimateCaloriesRequestData
import com.sadistictech.home.data.model.EstimateStepsDto
import com.sadistictech.home.data.model.EstimateStepsRequestData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface HomeApiService {

    @POST("food/estimate")
    suspend fun estimateCalories(
        @Body request: EstimateCaloriesRequestData,
    ): Response<EstimateCaloriesDto>

    @POST("activity/estimate")
    suspend fun estimateActivity(
        @Body request: EstimateActivityRequestData,
    ): Response<EstimateActivityDto>

    @POST("activity/steps")
    suspend fun estimateSteps(
        @Body request: EstimateStepsRequestData,
    ): Response<EstimateStepsDto>
}
