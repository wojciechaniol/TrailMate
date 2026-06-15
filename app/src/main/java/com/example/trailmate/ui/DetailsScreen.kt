package com.example.trailmate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.trailmate.model.Route
import com.example.trailmate.model.Stopwatch
import com.example.trailmate.model.Type
import kotlinx.coroutines.delay

@Composable
fun DetailsScreen(
    route: Route,
    stopwatch: Stopwatch,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
    onBack: () -> Unit
) {
    val accentColor = when (route.typeOfRoute) {
        Type.WALKING -> Color(0xFF4CAF82)
        Type.RUNNING -> Color(0xFFE8734A)
        Type.CYCLING -> Color(0xFF5B8DD9)
    }

    var displayMs by remember { mutableLongStateOf(stopwatch.currentElapsedMs) }
    LaunchedEffect(stopwatch.isRunning, stopwatch.currentElapsedMs) {
        if (stopwatch.isRunning) {
            while (true) {
                displayMs = stopwatch.currentElapsedMs
                delay(100)
            }
        } else {
            displayMs = stopwatch.currentElapsedMs
        }
    }

    var showFullScreenImage by rememberSaveable { mutableStateOf(false) }

    if (showFullScreenImage && route.imagePath != null) {
        FullScreenImageViewer(
            imagePath = route.imagePath,
            onDismiss = { showFullScreenImage = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(accentColor)
                .then(
                    if (route.imagePath != null)
                        Modifier.clickable { showFullScreenImage = true }
                    else Modifier
                )
        ) {
            if (route.imagePath != null) {
                AsyncImage(
                    model = route.imagePath,
                    contentDescription = "Route photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Icon(
                    imageVector = Icons.Default.ZoomIn,
                    contentDescription = "Tap the image to zoom in",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.25f), shape = CircleShape)
                )
            }

            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart)
                    .background(Color.Black.copy(alpha = 0.25f), shape = CircleShape)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Badge(label = route.typeOfRoute.name)
                Badge(label = route.difficulty.name)
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = route.name,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "%.1f km".format(route.length),
                    style = MaterialTheme.typography.titleMedium,
                    color = accentColor
                )
            }

            HorizontalDivider()

            Text(
                text = route.desc,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray
            )

            HorizontalDivider()

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Stopwatch",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Text(
                        text = formatElapsed(displayMs),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = if (stopwatch.isRunning) accentColor
                        else Color.DarkGray
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onReset,
                            modifier = Modifier.weight(1f),
                            enabled = (stopwatch.elapsedMs) > 0L || stopwatch.isRunning
                        ) {
                            Text("Reset")
                        }

                        Button(
                            onClick = if (stopwatch.isRunning) onStop else onStart,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (stopwatch.isRunning)
                                    Color(0xFFE57373) else accentColor
                            )
                        ) {
                            Text(if (stopwatch.isRunning) "Stop" else "Start")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Badge(label: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.Black.copy(alpha = 0.25f)
    ) {
        Text(
            text = label.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

private fun formatElapsed(ms: Long): String {
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) "%02d:%02d:%02d".format(hours, minutes, seconds)
    else "%02d:%02d".format(minutes, seconds)
}

@Composable
private fun FullScreenImageViewer(
    imagePath: String,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.92f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imagePath,
                contentDescription = "Full screen route photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(onClick = onDismiss),
                contentScale = ContentScale.Fit
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.White.copy(alpha = 0.15f), shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }
    }
}