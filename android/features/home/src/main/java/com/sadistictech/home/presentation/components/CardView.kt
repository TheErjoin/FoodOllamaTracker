package com.sadistictech.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val CardShape = RoundedCornerShape(20.dp)

@Composable
internal fun CardView(
    modifier: Modifier = Modifier,
    texts: List<String>,
) {
    require(texts.size in 1..2) {
        "CardView expects one value or a current/goal pair"
    }

    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = modifier.defaultMinSize(minWidth = 96.dp, minHeight = 96.dp),
        shape = CardShape,
        color = colors.surfaceContainerHigh,
        contentColor = colors.onSurface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        border = BorderStroke(
            width = 1.dp,
            color = colors.outlineVariant.copy(alpha = 0.7f),
        ),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            colors.surfaceContainerHigh,
                            colors.primaryContainer.copy(alpha = 0.45f),
                        ),
                    ),
                ),
        ) {
            val isCompact = maxWidth < 112.dp || maxHeight < 96.dp
            val isWide = maxWidth > maxHeight * 1.45f
            val contentPadding = if (isCompact) 10.dp else 14.dp
            val valueStyle = if (isCompact) {
                MaterialTheme.typography.titleMedium
            } else {
                MaterialTheme.typography.headlineSmall
            }.copy(
                fontWeight = FontWeight.SemiBold,
                fontFeatureSettings = "tnum",
            )

            CardContent(
                texts = texts,
                isWide = isWide,
                valueStyle = valueStyle,
                dividerColor = colors.primary.copy(alpha = 0.55f),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(if (isCompact) 8.dp else 12.dp)
                    .size(if (isCompact) 5.dp else 7.dp)
                    .background(
                        color = colors.primary.copy(alpha = 0.75f),
                        shape = CircleShape,
                    ),
            )
        }
    }
}

@Composable
private fun CardContent(
    texts: List<String>,
    isWide: Boolean,
    valueStyle: TextStyle,
    dividerColor: Color,
    modifier: Modifier = Modifier,
) {
    if (texts.size == 1) {
        ValueText(
            text = texts.first(),
            style = valueStyle,
            modifier = modifier,
        )
        return
    }

    if (isWide) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ValueText(
                text = texts[0],
                style = valueStyle,
                modifier = Modifier.weight(1f),
            )
            VerticalDivider(
                modifier = Modifier.fillMaxHeight(0.55f),
                thickness = 1.dp,
                color = dividerColor,
            )
            ValueText(
                text = texts[1],
                style = valueStyle,
                modifier = Modifier.weight(1f),
            )
        }
    } else {
        Column(modifier = modifier) {
            ValueText(
                text = texts[0],
                style = valueStyle,
                modifier = Modifier.weight(1f),
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(0.68f)
                    .align(Alignment.CenterHorizontally),
                thickness = 1.dp,
                color = dividerColor,
            )
            ValueText(
                text = texts[1],
                style = valueStyle,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun ValueText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface,
            style = style,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardViewPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(
                space = 12.dp,
                alignment = Alignment.Top,
            ),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    space = 12.dp,
                    alignment = Alignment.Start,
                ),
            ) {
                CardView(
                    modifier = Modifier.size(128.dp),
                    texts = listOf("1 200", "2 400"),
                )
                CardView(
                    modifier = Modifier.size(128.dp),
                    texts = listOf("10 000"),
                )
            }
            CardView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(88.dp),
                texts = listOf("1 200", "2 400"),
            )
        }
    }
}
