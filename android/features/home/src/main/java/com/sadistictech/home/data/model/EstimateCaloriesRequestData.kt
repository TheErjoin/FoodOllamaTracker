package com.sadistictech.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EstimateCaloriesRequestData(
    @SerialName("description")
    val description: String,
)
