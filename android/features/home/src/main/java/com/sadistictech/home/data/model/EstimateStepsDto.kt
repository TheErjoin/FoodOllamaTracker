package com.sadistictech.home.data.model

import com.sadistictech.data.utils.DataMapper
import com.sadistictech.home.domain.model.StepsEstimate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EstimateStepsDto(
    @SerialName("calories_burned")
    val caloriesBurned: Double,
    @SerialName("distance_km")
    val distanceKm: Double,
) : DataMapper<StepsEstimate> {

    override fun asDomain() = StepsEstimate(
        caloriesBurned = caloriesBurned,
        distanceKm = distanceKm,
    )
}
