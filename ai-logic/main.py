from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from models import (
    ActivityEstimate,
    ActivityEstimateRequest,
    FoodEstimateRequest,
    NutritionEstimate,
    StepsEstimate,
    StepsEstimateRequest,
)
from nutrition_service import (
    OLLAMA_MODEL,
    OLLAMA_URL,
    estimate_activity,
    estimate_food,
    estimate_steps,
)

app = FastAPI(
    title="Local AI Calculator",
    description="Numerical evaluation of food, activity, and steps",
    version="0.3.0",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
def home():
    return {"docs": "/docs", "version": app.version}


@app.get("/health")
def health():
    return {"ollama_url": OLLAMA_URL, "model": OLLAMA_MODEL}


@app.post("/food/estimate", response_model=NutritionEstimate)
async def food_estimate(data: FoodEstimateRequest):
    return await estimate_food(data.description)


@app.post("/activity/estimate", response_model=ActivityEstimate)
async def activity_estimate(data: ActivityEstimateRequest):
    return await estimate_activity(data)


@app.post("/activity/steps", response_model=StepsEstimate)
def steps_estimate(data: StepsEstimateRequest):
    return estimate_steps(data)
