package com.example.trailmate.model

import com.example.trailmate.data.DataSource
import kotlinx.coroutines.flow.Flow

class Repository(private val routeDao: RouteDao) {
    fun getRoutesByType(type: Type): Flow<List<Route>> =
        routeDao.getRoutesByType(type)

    fun getRouteWithStopwatch(routeId: Int): Flow<RouteWithStopwatch> =
        routeDao.getRouteWithStopwatch(routeId)

    suspend fun insertRoute(route: Route) =
        routeDao.insertRoute(route)

    suspend fun startStopwatch(routeId: Int, startTime: Long) {
        val existing: Stopwatch = routeDao.getStopwatch(routeId)
        routeDao.upsertStopwatch(
            existing.copy(startTime = startTime, isRunning = true)
        )
    }

    suspend fun pauseStopwatch(routeId: Int, elapsedMs: Long) {
        val existing = routeDao.getStopwatch(routeId)
        routeDao.upsertStopwatch(
            existing.copy(elapsedMs = elapsedMs, isRunning = false)
        )
    }

    suspend fun seedIfEmpty() {
        if (routeDao.getCount() == 0) {
            routeDao.insertAll(DataSource.Routes)
        }
    }
}