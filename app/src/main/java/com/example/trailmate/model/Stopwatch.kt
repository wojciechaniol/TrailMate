package com.example.trailmate.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "stopwatches",
    foreignKeys = [ForeignKey(
        entity = Route::class,
        parentColumns = ["id"],
        childColumns = ["routeId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("routeId")]
)
data class Stopwatch(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val routeId: Int,
    var startTime: Long,
    var elapsedMs: Long = 0L,
    var isRunning: Boolean = false
) {
    val currentElapsedMs: Long
        get() = if (isRunning) {
            elapsedMs + (System.currentTimeMillis() - startTime)
        } else {
            elapsedMs
        }
}
