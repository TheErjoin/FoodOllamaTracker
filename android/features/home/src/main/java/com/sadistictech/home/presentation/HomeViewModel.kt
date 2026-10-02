package com.sadistictech.home.presentation

import com.sadistictech.home.domain.model.ActivityEstimate
import com.sadistictech.home.domain.model.ActivityEstimateRequest
import com.sadistictech.home.domain.model.FoodEstimateRequest
import com.sadistictech.home.domain.model.NutritionEstimate
import com.sadistictech.home.domain.model.StepsEstimate
import com.sadistictech.home.domain.model.StepsEstimateRequest
import com.sadistictech.home.domain.usecase.EstimateActivityUseCase
import com.sadistictech.home.domain.usecase.EstimateFoodUseCase
import com.sadistictech.home.domain.usecase.EstimateStepsUseCase
import com.sadistictech.presentation.MutableUIStateFlow
import com.sadistictech.presentation.UIStateFlow
import com.sadistictech.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val estimateFoodUseCase: EstimateFoodUseCase,
    private val estimateActivityUseCase: EstimateActivityUseCase,
    private val estimateStepsUseCase: EstimateStepsUseCase,
) : BaseViewModel() {

    val foodEstimateState: UIStateFlow<NutritionEstimate>
        field = MutableUIStateFlow()

    val activityEstimateState: UIStateFlow<ActivityEstimate>
        field = MutableUIStateFlow()

    val stepsEstimateState: UIStateFlow<StepsEstimate>
        field = MutableUIStateFlow()

    fun estimateFood(request: FoodEstimateRequest) {
        estimateFoodUseCase(request).collectNetworkRequest(
            state = foodEstimateState,
            resetStateAfterCollect = false,
        )
    }

    fun estimateActivity(request: ActivityEstimateRequest) {
        estimateActivityUseCase(request).collectNetworkRequest(
            state = activityEstimateState,
            resetStateAfterCollect = false,
        )
    }

    fun estimateSteps(request: StepsEstimateRequest) {
        estimateStepsUseCase(request).collectNetworkRequest(
            state = stepsEstimateState,
            resetStateAfterCollect = false,
        )
    }
}
