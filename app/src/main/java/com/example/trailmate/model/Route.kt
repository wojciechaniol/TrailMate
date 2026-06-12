package com.example.trailmate.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Type {
    CYCLING,
    RUNNING,
    WALKING
}
enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}

@Entity(tableName = "routes")
data class Route(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val desc: String,
    val length: Double,
    val difficulty: Difficulty,
    val typeOfRoute: Type
)