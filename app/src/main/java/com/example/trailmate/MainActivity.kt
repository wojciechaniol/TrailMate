package com.example.trailmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.trailmate.model.AppDatabase
import com.example.trailmate.model.Repository
import com.example.trailmate.model.RoutesViewModel
import com.example.trailmate.ui.theme.CoursesTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(applicationContext)
        val repository = Repository(database.routeDao())
        val viewModel = RoutesViewModel(repository)

        lifecycleScope.launch {
            repository.seedIfEmpty()
        }

        setContent {
            CoursesTheme {
                RoutesApp(viewModel = viewModel)
            }
        }
    }
}
