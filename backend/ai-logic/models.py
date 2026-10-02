from typing import Optional

from pydantic import BaseModel, Field

class FoodEstimateRequest(BaseModel):
    description: str = Field(
        min_length=1,
        max_length=2000,
        description="What the user ate",
    )


class NutritionEstimate(BaseModel):
    calories: float = Field(ge=0)
    protein_g: float = Field(ge=0)
    fat_g: float = Field(ge=0)
    carbs_g: float = Field(ge=0)


class ActivityEstimateRequest(BaseModel):
    description: str = Field(
        min_length=1,
        max_length=1000,
        description="For example: fast walking for 45 minutes, or cycling for 30 minutes",
    )
    weight_kg: float = Field(gt=25, le=350)
    duration_minutes: Optional[float] = Field(default=None, gt=0, le=1440)


class ActivityEstimate(BaseModel):
    calories_burned: float = Field(ge=0)
    duration_minutes: float = Field(ge=0, le=1440)
    met: float = Field(ge=0, le=25)


class StepsEstimateRequest(BaseModel):
    steps: int = Field(ge=0, le=200000)
    weight_kg: float = Field(gt=25, le=350)
    height_cm: float = Field(ge=100, le=250)


class StepsEstimate(BaseModel):
    calories_burned: float = Field(ge=0)
    distance_km: float = Field(ge=0)
