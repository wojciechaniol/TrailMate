package com.example.trailmate.model

import androidx.room.Embedded
import androidx.room.Relation

data class RouteWithStopwatch(
    @Embedded val route: Route,
    @Relation(
        parentColumn = "id",
        entityColumn = "routeId"
    )
    val stopwatch: Stopwatch
)
