package com.sadistictech.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EstimateStepsRequestData(
    @SerialName("steps")
    val steps: Int,
    @SerialName("weight_kg")
    val weightKg: Double,
    @SerialName("height_cm")
    val heightCm: Double,
)
