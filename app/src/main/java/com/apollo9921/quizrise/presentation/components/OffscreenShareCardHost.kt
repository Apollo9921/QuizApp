package com.apollo9921.quizrise.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints

@Composable
fun OffscreenShareCardHost(
    data: ShareCardData,
    onGraphicsLayerReady: (GraphicsLayer) -> Unit
) {
    val graphicsLayer = rememberGraphicsLayer()

    Box(
        modifier = Modifier
            .layout { measurable, _ ->
                val placeable = measurable.measure(Constraints())
                layout(0, 0) {
                    placeable.placeRelative(placeable.width, placeable.height)
                }
            }
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
            }
    ) {
        QuizResultShareCard(data = data)
    }

    LaunchedEffect(graphicsLayer) {
        onGraphicsLayerReady(graphicsLayer)
    }
}