package com.sadistictech.home.data.model

import com.sadistictech.data.utils.DataMapper
import com.sadistictech.home.domain.model.NutritionEstimate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EstimateCaloriesDto(
    @SerialName("calories")
    val calories: Double,
    @SerialName("protein_g")
    val proteinGrams: Double,
    @SerialName("fat_g")
    val fatGrams: Double,
    @SerialName("carbs_g")
    val carbsGrams: Double,
) : DataMapper<NutritionEstimate> {

    override fun asDomain() = NutritionEstimate(
        calories = calories,
        proteinGrams = proteinGrams,
        fatGrams = fatGrams,
        carbsGrams = carbsGrams,
    )
}
