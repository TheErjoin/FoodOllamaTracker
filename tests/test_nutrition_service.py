import asyncio
import unittest
from unittest.mock import patch

from models import ActivityEstimateRequest, StepsEstimateRequest
from nutrition_service import estimate_activity, estimate_food, estimate_steps


class NutritionServiceTests(unittest.TestCase):
    def test_explicit_calories_are_preserved(self):
        ollama_json = """{
            "calories": 420,
            "protein_g": 30,
            "fat_g": 15,
            "carbs_g": 40
        }"""
        with patch("nutrition_service._ollama_chat", return_value=ollama_json):
            result = asyncio.run(
                estimate_food("боул с курицей на 500 калорий")
            )

        self.assertEqual(result.calories, 500)
        macro_calories = result.protein_g * 4 + result.fat_g * 9 + result.carbs_g * 4
        self.assertAlmostEqual(macro_calories, 500, delta=1)

    def test_unknown_food_returns_zero_without_ollama(self):
        with patch("nutrition_service._ollama_chat", side_effect=RuntimeError("offline")):
            result = asyncio.run(estimate_food("непонятное неизвестное блюдо"))

        self.assertEqual(result.calories, 0)
        self.assertEqual(result.protein_g, 0)
        self.assertEqual(result.fat_g, 0)
        self.assertEqual(result.carbs_g, 0)

    def test_explicit_calories_survive_fallback(self):
        with patch("nutrition_service._ollama_chat", side_effect=RuntimeError("offline")):
            result = asyncio.run(estimate_food("боул на 500 ккал"))

        self.assertEqual(result.calories, 500)
        self.assertEqual(result.protein_g, 0)

    def test_known_activity_fallback(self):
        request = ActivityEstimateRequest(
            description="быстрая ходьба 30 минут",
            weight_kg=80,
        )
        with patch("nutrition_service._ollama_chat", side_effect=RuntimeError("offline")):
            result = asyncio.run(estimate_activity(request))

        self.assertEqual(result.duration_minutes, 30)
        self.assertEqual(result.met, 3.5)
        self.assertEqual(result.calories_burned, 140)

    def test_unknown_activity_returns_zero(self):
        request = ActivityEstimateRequest(
            description="неизвестная активность",
            weight_kg=80,
        )
        with patch("nutrition_service._ollama_chat", side_effect=RuntimeError("offline")):
            result = asyncio.run(estimate_activity(request))

        self.assertEqual(result.calories_burned, 0)
        self.assertEqual(result.duration_minutes, 0)
        self.assertEqual(result.met, 0)

    def test_steps_are_calculated_without_ai(self):
        result = estimate_steps(
            StepsEstimateRequest(steps=10000, weight_kg=80, height_cm=180)
        )

        self.assertEqual(result.distance_km, 7.45)
        self.assertEqual(result.calories_burned, 298.1)


if __name__ == "__main__":
    unittest.main()
