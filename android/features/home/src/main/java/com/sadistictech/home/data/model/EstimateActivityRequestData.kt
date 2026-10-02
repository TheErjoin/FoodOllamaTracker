package com.sadistictech.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EstimateActivityRequestData(
    @SerialName("description")
    val description: String,
    @SerialName("weight_kg")
    val weightKg: Double,
    @SerialName("duration_minutes")
    val durationMinutes: Double? = null,
)
