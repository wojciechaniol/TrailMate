package com.example.trailmate.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.trailmate.model.RoutesViewModel

@Composable
fun RunningScreen(
    modifier: Modifier = Modifier,
    viewModel: RoutesViewModel,
    searchQuery: String = "",
    onRouteClick: (Int) -> Unit
) {
    val runningRoutes by viewModel.runningRoutes.collectAsStateWithLifecycle()
    val filtered = runningRoutes.filter { it.name.contains(searchQuery, ignoreCase = true) }

    GridWithTrails(filtered, 0xFFE8734A, onRouteClick)
}