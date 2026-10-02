package com.sadistictech.home.domain.repository

import com.sadistictech.domain.RemoteWrapper
import com.sadistictech.home.domain.model.ActivityEstimate
import com.sadistictech.home.domain.model.ActivityEstimateRequest
import com.sadistictech.home.domain.model.FoodEstimateRequest
import com.sadistictech.home.domain.model.NutritionEstimate
import com.sadistictech.home.domain.model.StepsEstimate
import com.sadistictech.home.domain.model.StepsEstimateRequest

interface HomeRepository {

    fun estimateFood(request: FoodEstimateRequest): RemoteWrapper<NutritionEstimate>

    fun estimateActivity(request: ActivityEstimateRequest): RemoteWrapper<ActivityEstimate>

    fun estimateSteps(request: StepsEstimateRequest): RemoteWrapper<StepsEstimate>
}
