package com.example.trailmate.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RoutesUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDarkTheme: Boolean = false
)

class RoutesViewModel(
    private val repository: Repository
) : ViewModel() {
    val walkingRoutes: StateFlow<List<Route>> = repository
        .getRoutesByType(Type.WALKING)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val runningRoutes: StateFlow<List<Route>> = repository
        .getRoutesByType(Type.RUNNING)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cyclingRoutes: StateFlow<List<Route>> = repository
        .getRoutesByType(Type.CYCLING)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _uiState = MutableStateFlow(RoutesUiState())
    val uiState: StateFlow<RoutesUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query )}
    }

    fun startStopwatch(routeId: Int) {
        viewModelScope.launch {
            repository.startStopwatch(routeId, System.currentTimeMillis())
        }
    }

    fun pauseStopwatch(routeId: Int, elapsedMs: Long) {
        viewModelScope.launch {
            repository.pauseStopwatch(routeId, elapsedMs)
        }
    }

    fun resetStopwatch(routeId: Int) {
        viewModelScope.launch {
            repository.resetStopwatch(routeId)
        }
    }

    fun addRoute(route: Route) {
        viewModelScope.launch {
            repository.insertRoute(route)
        }
    }

    fun getRouteWithStopwatch(id: Int): Flow<RouteWithStopwatch> =
        repository.getRouteWithStopwatch(id)

//    fun deleteRoute(route: Route) {
//        viewModelScope.launch {
//            repository.deleteRoute(route)
//        }
//    }
}