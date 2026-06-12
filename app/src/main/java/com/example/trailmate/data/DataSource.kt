package com.example.trailmate.data

import com.example.trailmate.model.Difficulty
import com.example.trailmate.model.Route
import com.example.trailmate.model.Type

object DataSource {
    val Routes = listOf(
        Route(name = "City Walk", desc = "Simple walk to enjoy the city of Poznan", length = 4.6, difficulty = Difficulty.EASY, typeOfRoute = Type.WALKING),
        Route(name = "Walk by the River", desc = "Picturesque walk by the Warta River", length = 6.8, difficulty = Difficulty.EASY, typeOfRoute = Type.WALKING),
        Route(name = "Power Walk", desc = "Fast walk to improve your stamina and strength", length = 3.2, difficulty = Difficulty.MEDIUM, typeOfRoute = Type.WALKING),
        Route(name = "Mountains Hiking", desc = "Nice walk to enjoy the views and breeze of fresh air", length = 10.6, difficulty = Difficulty.MEDIUM, typeOfRoute = Type.WALKING),
        Route(name = "Walk in the Forest", desc = "Pleasant walk to calm down and get in touch with nature", length = 2.5, difficulty = Difficulty.EASY, typeOfRoute = Type.WALKING),
        Route(name = "Cycling Tour", desc = "Long cycling route that takes you through Poznan and nearby area", length = 40.2, difficulty = Difficulty.MEDIUM, typeOfRoute = Type.CYCLING),
        Route(name = "Running in the Forest", desc = "Simple running route to help you get started", length = 2.5, difficulty = Difficulty.MEDIUM, typeOfRoute = Type.RUNNING),
        Route(name = "Fast and Simple Walk", desc = "If you don't have much time enjoy this fast route", length = 1.5, difficulty = Difficulty.EASY, typeOfRoute =  Type.WALKING),
        Route(name = "The Longest One", desc = "The longest route on this list", length = 15.4, difficulty = Difficulty.HARD, typeOfRoute = Type.WALKING),
        Route(name = "The Chosen One", desc = "The one to bring the perfect balance between running, power walking, cycling and walking", length = 12.0, difficulty = Difficulty.MEDIUM, typeOfRoute = Type.WALKING),
        Route(name = "Cultural Walk", desc = "City sightseeing walk perfect for people interested in cultural and historical part of the city", length = 4.2, difficulty = Difficulty.EASY, typeOfRoute = Type.WALKING),
    )
}
