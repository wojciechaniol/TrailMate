package com.example.trailmate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.grid.items
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.trailmate.model.Route
import com.example.trailmate.model.RoutesViewModel

@Composable
fun GridWithTrails(
    routes: List<Route>,
    color: Long,
    onRouteClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(routes) { route ->
            RouteCard(route = route, accentColor = Color(color), onClick = onRouteClick)
        }
    }
}

@Composable
fun WalkingScreen(
    modifier: Modifier = Modifier,
    viewModel: RoutesViewModel,
    searchQuery: String = "",
    onRouteClick: (Int) -> Unit
) {
    val walkingRoutes by viewModel.walkingRoutes.collectAsStateWithLifecycle()
    val filtered = walkingRoutes.filter { it.name.contains(searchQuery, ignoreCase = true) }

    GridWithTrails(filtered, 0xFF4CAF82, onRouteClick = onRouteClick)
}