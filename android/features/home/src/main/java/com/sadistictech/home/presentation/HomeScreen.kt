package com.sadistictech.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sadistictech.domain.NetworkError
import com.sadistictech.foodollamatracker.home.R
import com.sadistictech.home.domain.model.ActivityEstimate
import com.sadistictech.home.domain.model.ActivityEstimateRequest
import com.sadistictech.home.domain.model.FoodEstimateRequest
import com.sadistictech.home.domain.model.NutritionEstimate
import com.sadistictech.home.domain.model.StepsEstimate
import com.sadistictech.home.domain.model.StepsEstimateRequest
import com.sadistictech.home.presentation.components.CardView
import com.sadistictech.presentation.UIState
import java.text.NumberFormat

private const val MaxInputLength = 120
private const val TestWeightKg = 70.0
private const val TestHeightCm = 175.0
private const val TestSteps = 10_000
private const val DailyCaloriesGoal = 2_400

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val foodState by viewModel.foodEstimateState.collectAsStateWithLifecycle()
    val activityState by viewModel.activityEstimateState.collectAsStateWithLifecycle()
    val stepsState by viewModel.stepsEstimateState.collectAsStateWithLifecycle()

    HomeContent(
        foodState = foodState,
        activityState = activityState,
        stepsState = stepsState,
        onEstimateFood = { description ->
            viewModel.estimateFood(FoodEstimateRequest(description = description))
        },
        onEstimateActivity = { description ->
            viewModel.estimateActivity(
                ActivityEstimateRequest(
                    description = description,
                    weightKg = TestWeightKg,
                ),
            )
        },
        onEstimateSteps = {
            viewModel.estimateSteps(
                StepsEstimateRequest(
                    steps = TestSteps,
                    weightKg = TestWeightKg,
                    heightCm = TestHeightCm,
                ),
            )
        },
    )
}

@Composable
private fun HomeContent(
    foodState: UIState<NutritionEstimate>,
    activityState: UIState<ActivityEstimate>,
    stepsState: UIState<StepsEstimate>,
    onEstimateFood: (String) -> Unit,
    onEstimateActivity: (String) -> Unit,
    onEstimateSteps: () -> Unit,
) {
    var foodText by rememberSaveable { mutableStateOf("") }
    var activityText by rememberSaveable { mutableStateOf("") }
    val activityFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val submitFood: () -> Unit = {
        foodText.trim().takeIf(String::isNotEmpty)?.let(onEstimateFood)
    }
    val submitActivity: () -> Unit = {
        activityText.trim().takeIf(String::isNotEmpty)?.let(onEstimateActivity)
        focusManager.clearFocus()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.today_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(20.dp))

            SummaryCards(foodState = foodState, steps = TestSteps)

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onEstimateSteps,
                enabled = stepsState !is UIState.Loading,
            ) {
                RequestButtonContent(
                    isLoading = stepsState is UIState.Loading,
                    text = stringResource(R.string.estimate_test_steps),
                )
            }
            RequestStateMessage(state = stepsState) { estimate ->
                stringResource(
                    R.string.steps_result,
                    estimate.distanceKm.asDisplayNumber(),
                    estimate.caloriesBurned.asDisplayNumber(),
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            TrackerInputSection(
                title = stringResource(R.string.food_section_title),
                value = foodText,
                onValueChange = { foodText = it.take(MaxInputLength) },
                label = stringResource(R.string.food_field_label),
                placeholder = stringResource(R.string.food_field_placeholder),
                actionText = stringResource(R.string.estimate_food),
                isLoading = foodState is UIState.Loading,
                onSubmit = submitFood,
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(
                    onNext = { activityFocusRequester.requestFocus() },
                ),
            )
            RequestStateMessage(state = foodState) { estimate ->
                stringResource(
                    R.string.food_result,
                    estimate.calories.asDisplayNumber(),
                    estimate.proteinGrams.asDisplayNumber(),
                    estimate.fatGrams.asDisplayNumber(),
                    estimate.carbsGrams.asDisplayNumber(),
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            TrackerInputSection(
                title = stringResource(R.string.activity_section_title),
                value = activityText,
                onValueChange = { activityText = it.take(MaxInputLength) },
                label = stringResource(R.string.activity_field_label),
                placeholder = stringResource(R.string.activity_field_placeholder),
                actionText = stringResource(R.string.estimate_activity),
                isLoading = activityState is UIState.Loading,
                onSubmit = submitActivity,
                modifier = Modifier.focusRequester(activityFocusRequester),
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = { submitActivity() }),
            )
            Text(
                text = stringResource(R.string.test_profile_hint, TestWeightKg.toInt()),
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp)
                    .padding(top = 6.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
            RequestStateMessage(state = activityState) { estimate ->
                stringResource(
                    R.string.activity_result,
                    estimate.caloriesBurned.asDisplayNumber(),
                    estimate.durationMinutes.asDisplayNumber(),
                    estimate.met.asDisplayNumber(),
                )
            }
        }
    }
}

@Composable
private fun SummaryCards(
    foodState: UIState<NutritionEstimate>,
    steps: Int,
) {
    val currentCalories = (foodState as? UIState.Success)?.data?.calories ?: 0.0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 360.dp),
        horizontalArrangement = Arrangement.spacedBy(
            space = 12.dp,
            alignment = Alignment.CenterHorizontally,
        ),
    ) {
        SummaryCard(
            title = stringResource(R.string.calories_title),
            texts = listOf(
                currentCalories.asDisplayNumber(),
                DailyCaloriesGoal.toString(),
            ),
            modifier = Modifier.weight(1f),
        )
        SummaryCard(
            title = stringResource(R.string.steps_title),
            texts = listOf(NumberFormat.getIntegerInstance().format(steps)),
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    texts: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 8.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
        )
        CardView(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            texts = texts,
        )
    }
}

@Composable
private fun TrackerInputSection(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    actionText: String,
    isLoading: Boolean,
    onSubmit: () -> Unit,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 520.dp),
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            enabled = !isLoading,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            trailingIcon = if (value.isNotEmpty()) {
                {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.clear_field),
                        )
                    }
                }
            } else {
                null
            },
            supportingText = {
                Text(stringResource(R.string.character_counter, value.length, MaxInputLength))
            },
            shape = RoundedCornerShape(16.dp),
            minLines = 1,
            maxLines = 3,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = imeAction,
            ),
            keyboardActions = keyboardActions,
        )
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 10.dp),
            enabled = value.isNotBlank() && !isLoading,
        ) {
            RequestButtonContent(isLoading = isLoading, text = actionText)
        }
    }
}

@Composable
private fun RequestButtonContent(
    isLoading: Boolean,
    text: String,
) {
    if (isLoading) {
        CircularProgressIndicator(
            modifier = Modifier.size(18.dp),
            strokeWidth = 2.dp,
        )
        Spacer(modifier = Modifier.size(8.dp))
    }
    Text(text = if (isLoading) stringResource(R.string.request_in_progress) else text)
}

@Composable
private fun <T> RequestStateMessage(
    state: UIState<T>,
    successMessage: @Composable (T) -> String,
) {
    val message = when (state) {
        UIState.Idle,
        UIState.Loading,
        -> return

        is UIState.Error -> state.error.asDisplayMessage()
        is UIState.Success -> successMessage(state.data)
    }

    val isError = state is UIState.Error
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 520.dp)
            .padding(top = 10.dp),
        shape = RoundedCornerShape(12.dp),
        color = if (isError) {
            MaterialTheme.colorScheme.errorContainer
        } else {
            MaterialTheme.colorScheme.secondaryContainer
        },
        contentColor = if (isError) {
            MaterialTheme.colorScheme.onErrorContainer
        } else {
            MaterialTheme.colorScheme.onSecondaryContainer
        },
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun NetworkError.asDisplayMessage(): String = when (this) {
    NetworkError.Timeout -> stringResource(R.string.error_timeout)
    is NetworkError.ApiInputs -> errors.values.flatten().joinToString().ifBlank {
        stringResource(R.string.error_invalid_input)
    }
    is NetworkError.Api -> message.ifBlank { stringResource(R.string.error_server) }
    is NetworkError.Unexpected -> message.ifBlank { stringResource(R.string.error_unexpected) }
}

private fun Double.asDisplayNumber(): String = NumberFormat.getNumberInstance().apply {
    maximumFractionDigits = 1
}.format(this)

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeContent(
            foodState = UIState.Success(
                NutritionEstimate(
                    calories = 105.0,
                    proteinGrams = 1.3,
                    fatGrams = 0.4,
                    carbsGrams = 27.0,
                ),
            ),
            activityState = UIState.Idle,
            stepsState = UIState.Idle,
            onEstimateFood = {},
            onEstimateActivity = {},
            onEstimateSteps = {},
        )
    }
}
