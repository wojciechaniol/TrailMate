package com.example.trailmate.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RouteDao {
    @Transaction
    @Query("SELECT * FROM routes WHERE id = :routeId")
    fun getRouteWithStopwatch(routeId: Int): Flow<RouteWithStopwatch>

    @Query("SELECT COUNT(*) FROM routes")
    suspend fun getCount(): Int

    @Query("SELECT * FROM routes")
    suspend fun getAllRoutes(): List<Route>

    @Insert
    suspend fun insertAll(routes: List<Route>)

    @Query("SELECT * FROM routes WHERE typeOfRoute = :type")
    fun getRoutesByType(type: Type): Flow<List<Route>>

    @Query("SELECT * FROM stopwatches WHERE routeId= :id")
    suspend fun getStopwatch(id: Int): Stopwatch

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStopwatch(stopwatch: Stopwatch)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoute(route: Route): Long
}