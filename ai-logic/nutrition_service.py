import asyncio
import json
import os
import re
from typing import Dict, List, Optional, Tuple
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen

from pydantic import BaseModel, Field, ValidationError

from models import (
    ActivityEstimate,
    ActivityEstimateRequest,
    NutritionEstimate,
    StepsEstimate,
    StepsEstimateRequest,
)


OLLAMA_URL = os.getenv("OLLAMA_URL", "http://127.0.0.1:11434").rstrip("/")
OLLAMA_MODEL = os.getenv("OLLAMA_MODEL", "gemma3:4b")

SYSTEM_PROMPT = """Ты — калькулятор еды и физической активности.
Возвращай только JSON по переданной схеме и только числовые значения.
Не добавляй советы, объяснения или текст. Если данных недостаточно и оценить
значение нельзя, верни 0 в соответствующем поле. Если пользователь явно указал
калории, сохрани это число без изменений.
"""


class OllamaActivityPayload(BaseModel):
    duration_minutes: float = Field(ge=0, le=1440)
    met: float = Field(ge=0, le=25)


# Значения на 100 г: калории, белки, жиры, углеводы.
FOOD_CATALOG: List[Tuple[str, Tuple[str, ...], float, float, float, float]] = [
    ("гречка", ("гречк",), 110, 4.2, 1.1, 21.3),
    ("куриная грудка", ("кур", "груд"), 165, 31.0, 3.6, 0.0),
    ("рис", ("рис",), 130, 2.7, 0.3, 28.0),
    ("овсянка", ("овсян",), 71, 2.5, 1.5, 12.0),
    ("яйцо", ("яйц",), 155, 13.0, 11.0, 1.1),
    ("банан", ("банан",), 89, 1.1, 0.3, 23.0),
    ("яблоко", ("яблок",), 52, 0.3, 0.2, 14.0),
    ("хлеб", ("хлеб",), 250, 8.0, 3.3, 49.0),
    ("сыр", ("сыр",), 350, 25.0, 27.0, 2.0),
    ("творог", ("творог",), 121, 17.0, 5.0, 1.8),
    ("картофель", ("карто",), 87, 1.9, 0.1, 20.0),
    ("говядина", ("говя",), 250, 26.0, 15.0, 0.0),
    ("лосось", ("лосос",), 208, 20.0, 13.0, 0.0),
    ("молоко", ("молок",), 52, 3.0, 2.5, 4.8),
    ("кефир", ("кефир",), 53, 2.9, 2.5, 4.0),
    ("масло", ("масл",), 899, 0.0, 99.9, 0.0),
]

# MET для распространённых активностей.
ACTIVITY_CATALOG: List[Tuple[Tuple[str, ...], float]] = [
    (("бег", "пробеж"), 8.3),
    (("ходьб", "гуля", "шаг"), 3.5),
    (("велосип",), 7.5),
    (("силов", "тренаж", "штанг", "гантел"), 6.0),
    (("плав", "бассейн"), 6.0),
    (("йог",), 2.5),
    (("уборк",), 3.3),
    (("футбол",), 7.0),
    (("баскетбол",), 6.5),
    (("лестниц",), 8.0),
]


def _ollama_chat(prompt: str, schema: Dict) -> str:
    body = {
        "model": OLLAMA_MODEL,
        "stream": False,
        "messages": [
            {"role": "system", "content": SYSTEM_PROMPT},
            {"role": "user", "content": prompt},
        ],
        "format": schema,
        "options": {"temperature": 0},
    }
    request = Request(
        f"{OLLAMA_URL}/api/chat",
        data=json.dumps(body).encode("utf-8"),
        headers={"Content-Type": "application/json"},
        method="POST",
    )

    try:
        with urlopen(request, timeout=120) as response:
            payload = json.loads(response.read().decode("utf-8"))
    except HTTPError as error:
        details = error.read().decode("utf-8", errors="replace")
        raise RuntimeError(f"Ollama вернул ошибку {error.code}: {details}") from error
    except (URLError, TimeoutError) as error:
        raise RuntimeError("Ollama недоступна") from error

    content = payload.get("message", {}).get("content")
    if not content:
        raise RuntimeError("Ollama вернула пустой ответ")
    return content.strip()


def _zero_nutrition() -> NutritionEstimate:
    return NutritionEstimate(calories=0, protein_g=0, fat_g=0, carbs_g=0)


def _explicit_calories(text: str) -> Optional[float]:
    match = re.search(
        r"(\d+(?:[.,]\d+)?)\s*(?:ккал|кал+ори(?:й|я|и)?)",
        text.lower(),
    )
    if not match:
        return None
    return float(match.group(1).replace(",", "."))


def _extract_grams(text: str) -> Optional[float]:
    match = re.search(r"(\d+(?:[.,]\d+)?)\s*(?:г|гр|грамм(?:а|ов)?)\b", text)
    if not match:
        return None
    return float(match.group(1).replace(",", "."))


def _normalize_nutrition(
    value: NutritionEstimate, explicit_calories: Optional[float]
) -> NutritionEstimate:
    calories = explicit_calories if explicit_calories is not None else value.calories
    protein = value.protein_g
    fat = value.fat_g
    carbs = value.carbs_g

    # Если калории заданы явно, приводим примерное КБЖУ к той же энергетической сумме.
    macro_calories = protein * 4 + fat * 9 + carbs * 4
    if explicit_calories is not None and explicit_calories > 0 and macro_calories > 0:
        factor = explicit_calories / macro_calories
        protein *= factor
        fat *= factor
        carbs *= factor

    return NutritionEstimate(
        calories=round(calories, 1),
        protein_g=round(protein, 1),
        fat_g=round(fat, 1),
        carbs_g=round(carbs, 1),
    )


async def estimate_food(description: str) -> NutritionEstimate:
    explicit_calories = _explicit_calories(description)
    prompt = f"""Определи суммарную пищевую ценность описанной еды.
Описание: {description}

Поля ответа: calories, protein_g, fat_g, carbs_g.
Если калории прямо написаны пользователем, calories должно быть равно этому числу.
Если КБЖУ неизвестно, оцени его по типичному составу блюда. Если оценка невозможна,
верни 0 в неизвестных полях.
"""
    try:
        raw = await asyncio.to_thread(
            _ollama_chat,
            prompt,
            NutritionEstimate.model_json_schema(),
        )
        result = NutritionEstimate.model_validate_json(raw)
        if result == _zero_nutrition():
            return fallback_food_estimate(description)
        return _normalize_nutrition(result, explicit_calories)
    except (RuntimeError, ValidationError, ValueError, json.JSONDecodeError):
        return fallback_food_estimate(description)


def fallback_food_estimate(description: str) -> NutritionEstimate:
    explicit_calories = _explicit_calories(description)
    calories = 0.0
    protein = 0.0
    fat = 0.0
    carbs = 0.0
    found = False

    segments = re.split(r",|;|\n|\s+и\s+", description.lower())
    for segment in segments:
        for _, tokens, kcal, item_protein, item_fat, item_carbs in FOOD_CATALOG:
            if not all(token in segment for token in tokens):
                continue
            grams = _extract_grams(segment) or 100.0
            factor = grams / 100.0
            calories += kcal * factor
            protein += item_protein * factor
            fat += item_fat * factor
            carbs += item_carbs * factor
            found = True
            break

    if not found and explicit_calories is None:
        return _zero_nutrition()

    result = NutritionEstimate(
        calories=round(calories, 1),
        protein_g=round(protein, 1),
        fat_g=round(fat, 1),
        carbs_g=round(carbs, 1),
    )
    return _normalize_nutrition(result, explicit_calories)


def _duration_from_text(text: str) -> float:
    minute_match = re.search(r"(\d+(?:[.,]\d+)?)\s*(?:мин|минут)", text)
    if minute_match:
        return float(minute_match.group(1).replace(",", "."))
    hour_match = re.search(r"(\d+(?:[.,]\d+)?)\s*(?:ч|час)", text)
    if hour_match:
        return float(hour_match.group(1).replace(",", ".")) * 60
    return 0.0


def _known_met(text: str) -> float:
    lowered = text.lower()
    for keywords, met in ACTIVITY_CATALOG:
        if any(keyword in lowered for keyword in keywords):
            return met
    return 0.0


def _activity_result(weight_kg: float, duration_minutes: float, met: float):
    if duration_minutes <= 0 or met <= 0:
        return ActivityEstimate(calories_burned=0, duration_minutes=0, met=0)
    calories = met * weight_kg * duration_minutes / 60.0
    return ActivityEstimate(
        calories_burned=round(calories, 1),
        duration_minutes=round(duration_minutes, 1),
        met=round(met, 1),
    )


async def estimate_activity(data: ActivityEstimateRequest) -> ActivityEstimate:
    prompt = f"""Определи только длительность и MET физической активности.
Описание: {data.description}
Переданная длительность: {data.duration_minutes}

Верни duration_minutes и met. Если определить значение нельзя, верни 0.
"""
    try:
        raw = await asyncio.to_thread(
            _ollama_chat,
            prompt,
            OllamaActivityPayload.model_json_schema(),
        )
        payload = OllamaActivityPayload.model_validate_json(raw)
        duration = data.duration_minutes or payload.duration_minutes
        met = _known_met(data.description) or payload.met
        return _activity_result(data.weight_kg, duration, met)
    except (RuntimeError, ValidationError, ValueError, json.JSONDecodeError):
        duration = data.duration_minutes or _duration_from_text(data.description)
        met = _known_met(data.description)
        return _activity_result(data.weight_kg, duration, met)


def estimate_steps(data: StepsEstimateRequest) -> StepsEstimate:
    # Средняя длина шага приблизительно равна 41.4% роста.
    step_length_m = data.height_cm * 0.414 / 100.0
    distance_km = data.steps * step_length_m / 1000.0
    # При ходьбе расход около 0.5 ккал на кг массы на километр.
    calories = 0.5 * data.weight_kg * distance_km
    return StepsEstimate(
        calories_burned=round(calories, 1),
        distance_km=round(distance_km, 2),
    )
