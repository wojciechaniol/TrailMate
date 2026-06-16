package com.example.trailmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.example.trailmate.model.AppDatabase
import com.example.trailmate.model.Repository
import com.example.trailmate.model.RoutesViewModel
import kotlinx.coroutines.launch
import androidx.core.content.edit
import com.example.trailmate.ui.theme.TrailMateTheme

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
            var isDarkTheme by rememberSaveable { mutableStateOf(loadThemePreference()) }
            TrailMateTheme(darkTheme = isDarkTheme) {
                RoutesApp(
                    viewModel = viewModel,
                    isDarkTheme = isDarkTheme,
                    onToggleTheme = {
                        isDarkTheme = !isDarkTheme
                        saveThemePreference(isDarkTheme)
                    }
                )
            }
        }
    }

    private fun loadThemePreference(): Boolean {
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        return prefs.getBoolean("dark_mode", false) // default: light
    }

    private fun saveThemePreference(isDark: Boolean) {
        getSharedPreferences("settings", MODE_PRIVATE)
            .edit()
            .putBoolean("dark_mode", isDark)
            .apply()
    }
}
