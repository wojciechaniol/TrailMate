package com.example.trailmate

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.trailmate.model.RoutesViewModel
import com.example.trailmate.ui.AboutScreen
import com.example.trailmate.ui.AddRouteScreen
import com.example.trailmate.ui.CyclingScreen
import com.example.trailmate.ui.MainScreen
import com.example.trailmate.ui.RunningScreen
import com.example.trailmate.ui.WalkingScreen
import com.example.trailmate.ui.DetailsScreen
import com.example.trailmate.ui.SettingsScreen
import kotlinx.coroutines.launch

enum class RoutesScreen(val title: String) {
    Start(title = "Main"),
    Running(title = "Running"),
    Cycling(title = "Cycling"),
    Walking(title = "Walking"),
    AddRoute(title = "Adding"),
    Details(title="Details"),
    Settings(title="Settings"),
    About(title="About")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutesTopBar(
    currentScreen: RoutesScreen,
    onNavigate: (RoutesScreen) -> Unit,
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    var searchVisible by rememberSaveable { mutableStateOf(false) }
    var menuExpanded by rememberSaveable { mutableStateOf(false) }

    val navTabs = listOf(
        RoutesScreen.Start    to "All",
        RoutesScreen.Walking  to "Walking",
        RoutesScreen.Running  to "Running",
        RoutesScreen.Cycling  to "Cycling",
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Settings") },
                        onClick = {
                            menuExpanded = false
                            onSettingsClick()
                        },
                        leadingIcon = { Icon(Icons.Default.Settings, null) }
                    )
                    DropdownMenuItem(
                        text = { Text("About") },
                        onClick = {
                            menuExpanded = false
                            onAboutClick()
                        },
                        leadingIcon = { Icon(Icons.Default.Info, null) }
                    )
                }
            }

            Text(
                text = "TrailMate",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            IconButton(onClick = {
                searchVisible = !searchVisible
                if (!searchVisible) onSearchQueryChange("")
            }) {
                Icon(
                    imageVector = if (searchVisible) Icons.Default.Close
                    else Icons.Default.Search,
                    contentDescription = if (searchVisible) "Close search" else "Search"
                )
            }
        }

        AnimatedVisibility(
            visible = searchVisible,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search routes…") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(24.dp)
            )
        }

        TabRow(
            selectedTabIndex = navTabs.indexOfFirst { it.first == currentScreen }
                .coerceAtLeast(0),
            divider = {}
        ) {
            navTabs.forEach { (screen, label) ->
                Tab(
                    selected = currentScreen == screen,
                    onClick = { onNavigate(screen) },
                    text = {
                        Text(
                            text = label,
                            fontWeight = if (currentScreen == screen)
                                FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        HorizontalDivider()
    }
}

@Composable
fun RoutesApp(
    viewModel: RoutesViewModel,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val tabs = listOf(
        RoutesScreen.Start,
        RoutesScreen.Walking,
        RoutesScreen.Running,
        RoutesScreen.Cycling,
    )

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val isOnAddRoute = backStackEntry?.destination?.route == RoutesScreen.AddRoute.name
    val isOnDetails = backStackEntry?.destination?.route?.startsWith(RoutesScreen.Details.name) == true
    val isOnSettings = backStackEntry?.destination?.route == RoutesScreen.Settings.name
    val isOnAbout = backStackEntry?.destination?.route == RoutesScreen.About.name
    val currentScreen = tabs[pagerState.currentPage]

    var searchQuery by rememberSaveable { mutableStateOf("") }

    val onRouteClick: (Int) -> Unit = { routeId ->
        navController.navigate("${RoutesScreen.Details.name}/$routeId")
    }

    Scaffold(
        topBar = {
            if (!isOnAddRoute && !isOnDetails && !isOnSettings && !isOnAbout) {
                RoutesTopBar(
                    currentScreen = currentScreen,
                    onNavigate = { screen ->
                        val index = tabs.indexOf(screen)
                        if (index >= 0) {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    },
                    onSettingsClick = { navController.navigate(RoutesScreen.Settings.name) },
                    onAboutClick = { navController.navigate(RoutesScreen.About.name) },
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it }
                )
            }
        },
        floatingActionButton = {
            if (!isOnSettings && !isOnAbout) {
                FloatingActionButton(
                    onClick = { navController.navigate(RoutesScreen.AddRoute.name) }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add route"
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RoutesScreen.Start.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(200))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(200))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(200))
            }
        ) {
            composable(route = RoutesScreen.Start.name) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (tabs[page]) {
                        RoutesScreen.Start    -> MainScreen(
                            viewModel = viewModel,
                            searchQuery = searchQuery,
                            onRouteClick = onRouteClick)
                        RoutesScreen.Walking  -> WalkingScreen(
                            viewModel = viewModel,
                            searchQuery = searchQuery,
                            onRouteClick = onRouteClick)
                        RoutesScreen.Running  -> RunningScreen(
                            viewModel = viewModel,
                            searchQuery = searchQuery,
                            onRouteClick = onRouteClick)
                        RoutesScreen.Cycling  -> CyclingScreen(
                            viewModel = viewModel,
                            searchQuery = searchQuery,
                            onRouteClick = onRouteClick)
                        else                  -> {}
                    }
                }
            }
            composable(route = RoutesScreen.AddRoute.name) {
                AddRouteScreen(
                    viewModel = viewModel,
                    onRouteAdded = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() }
                )
            }

            composable(
                route = "${RoutesScreen.Details.name}/{routeId}",
                arguments = listOf(navArgument("routeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val routeId = backStackEntry.arguments?.getInt("routeId") ?: return@composable
                val routeWithStopwatch by viewModel
                    .getRouteWithStopwatch(routeId)
                    .collectAsStateWithLifecycle(null)

                routeWithStopwatch?.let {
                    DetailsScreen(
                        route = it.route,
                        stopwatch = it.stopwatch,
                        onStart  = { viewModel.startStopwatch(routeId) },
                        onStop = { it.stopwatch.let { sw ->
                            viewModel.pauseStopwatch(routeId, sw.currentElapsedMs)
                        }},
                        onReset  = { viewModel.resetStopwatch(routeId) },
                        onBack   = { navController.popBackStack() }
                    )
                }
            }

            composable(route = RoutesScreen.Settings.name) {
                SettingsScreen(
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = onToggleTheme,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(route = RoutesScreen.About.name) {
                AboutScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}