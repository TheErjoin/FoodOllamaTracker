package com.sadistictech.home.domain.model

data class ActivityEstimateRequest(
    val description: String,
    val weightKg: Double,
    val durationMinutes: Double? = null,
)
