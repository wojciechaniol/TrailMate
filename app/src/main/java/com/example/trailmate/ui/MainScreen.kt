package com.example.trailmate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.trailmate.model.Route
import com.example.trailmate.model.RoutesViewModel

private fun List<Route>.filterByQuery(query: String) =
    filter { it.name.contains(query, ignoreCase = true) }

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: RoutesViewModel,
    searchQuery: String = "",
    onRouteClick: (Int) -> Unit
) {
    val walkingRoutes by viewModel.walkingRoutes.collectAsStateWithLifecycle()
    val cyclingRoutes by viewModel.cyclingRoutes.collectAsStateWithLifecycle()
    val runningRoutes by viewModel.runningRoutes.collectAsStateWithLifecycle()

    val filteredWalking = walkingRoutes.filterByQuery(searchQuery)
    val filteredRunning = runningRoutes.filterByQuery(searchQuery)
    val filteredCycling = cyclingRoutes.filterByQuery(searchQuery)

    val sections = listOf(
        Triple("Walking",  filteredWalking,  Color(0xFF4CAF82)),
        Triple("Running",  filteredRunning,  Color(0xFFE8734A)),
        Triple("Cycling",  filteredCycling,  Color(0xFF5B8DD9)),
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        sections.forEach { (title, routes, accentColor) ->
            item {
                RouteSection(
                    title = title,
                    routes = routes,
                    accentColor = accentColor,
                    onRouteClick = onRouteClick
                )
            }
        }
    }
}

@Composable
fun RouteSection(
    title: String,
    routes: List<Route>,
    accentColor: Color,
    onRouteClick: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        if (routes.isEmpty()) {
            Text(
                text = "No routes",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(routes) { route ->
                    RouteCard(
                        route = route,
                        accentColor = accentColor,
                        onClick = onRouteClick
                    )
                }
            }
        }
    }
}

@Composable
fun RouteCard(
    route: Route,
    accentColor: Color,
    onClick: (Int) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .wrapContentHeight()
            .clickable { onClick(route.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(accentColor),
                contentAlignment = Alignment.BottomStart
            ) {
                if (route.imagePath != null) {
                    AsyncImage(
                        model = route.imagePath,
                        contentDescription = "Route photo",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Surface(
                    shape = RoundedCornerShape(
                        topStart = 0.dp, topEnd = 0.dp,
                        bottomEnd = 6.dp, bottomStart = 0.dp
                    ),
                    color = Color.Black.copy(alpha = 0.25f)
                ) {
                    Text(
                        text = route.difficulty.name
                            .lowercase()
                            .replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = route.name,
                    style = MaterialTheme.typography.titleSmall
                        .copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "%.1f km".format(route.length),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}