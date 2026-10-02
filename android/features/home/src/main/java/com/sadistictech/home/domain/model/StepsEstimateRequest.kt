package com.sadistictech.home.domain.model

data class StepsEstimateRequest(
    val steps: Int,
    val weightKg: Double,
    val heightCm: Double,
)
