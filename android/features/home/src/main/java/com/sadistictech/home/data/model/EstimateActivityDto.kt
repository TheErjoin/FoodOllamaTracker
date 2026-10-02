package com.sadistictech.home.data.model

import com.sadistictech.data.utils.DataMapper
import com.sadistictech.home.domain.model.ActivityEstimate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EstimateActivityDto(
    @SerialName("calories_burned")
    val caloriesBurned: Double,
    @SerialName("duration_minutes")
    val durationMinutes: Double,
    @SerialName("met")
    val met: Double,
) : DataMapper<ActivityEstimate> {

    override fun asDomain() = ActivityEstimate(
        caloriesBurned = caloriesBurned,
        durationMinutes = durationMinutes,
        met = met,
    )
}
