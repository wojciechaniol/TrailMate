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
fun CyclingScreen(
    modifier: Modifier = Modifier,
    viewModel: RoutesViewModel
) {
    val cyclingRoutes by viewModel.cyclingRoutes.collectAsStateWithLifecycle()

    LazyColumn() {
        items(cyclingRoutes) { route -> Text(route.name) }
    }
}