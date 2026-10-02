package com.sadistictech.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sadistictech.foodollamatracker.home.R
import com.sadistictech.home.presentation.components.CardView

private const val MaxInputLength = 120

@Composable
fun HomeScreen() {
    var foodText by rememberSaveable { mutableStateOf("") }
    var activityText by rememberSaveable { mutableStateOf("") }
    val activityFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

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
                text = "Сегодня",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(20.dp))

            SummaryCards()

            Spacer(modifier = Modifier.height(28.dp))

            TrackerInputSection(
                title = stringResource(R.string.food_section_title),
                value = foodText,
                onValueChange = { foodText = it.take(MaxInputLength) },
                label = stringResource(R.string.food_field_label),
                placeholder = stringResource(R.string.food_field_placeholder),
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(
                    onNext = { activityFocusRequester.requestFocus() },
                ),
            )

            Spacer(modifier = Modifier.height(22.dp))

            TrackerInputSection(
                title = stringResource(R.string.activity_section_title),
                value = activityText,
                onValueChange = { activityText = it.take(MaxInputLength) },
                label = stringResource(R.string.activity_field_label),
                placeholder = stringResource(R.string.activity_field_placeholder),
                modifier = Modifier.focusRequester(activityFocusRequester),
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() },
                ),
            )
        }
    }
}

@Composable
private fun SummaryCards() {
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
            texts = listOf("1 200", "2 400"),
            modifier = Modifier.weight(1f),
        )
        SummaryCard(
            title = stringResource(R.string.steps_title),
            texts = listOf("10 000"),
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TrackerInputSection(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
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
                Text(
                    text = stringResource(
                        R.string.character_counter,
                        value.length,
                        MaxInputLength,
                    ),
                )
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
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}
