package com.example.trailmate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.trailmate.model.Route
import com.example.trailmate.model.RoutesViewModel
import com.example.trailmate.model.Difficulty
import com.example.trailmate.model.Type

@Composable
fun AddRouteScreen(
    viewModel: RoutesViewModel,
    onRouteAdded: () -> Unit,
    onCancel: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var length by rememberSaveable { mutableStateOf("") }
    var selectedType by rememberSaveable { mutableStateOf(Type.RUNNING.name) }
    var selectedDifficulty by rememberSaveable { mutableStateOf(Difficulty.EASY.name) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Route name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = length,
            onValueChange = { length = it },
            label = { Text("Length (km)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        // Type selector
        Text("Type")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Type.entries.forEach { type ->
                FilterChip(
                    selected = selectedType == type.name,
                    onClick = { selectedType = type.name },
                    label = { Text(type.name) }
                )
            }
        }

        // Difficulty selector
        Text("Difficulty")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Difficulty.entries.forEach { difficulty ->
                FilterChip(
                    selected = selectedDifficulty == difficulty.name,
                    onClick = { selectedDifficulty = difficulty.name },
                    label = { Text(difficulty.name) }
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    viewModel.addRoute(
                        Route(
                            name = name,
                            desc = description,
                            length = length.toDoubleOrNull() ?: 0.0,
                            difficulty = Difficulty.valueOf(selectedDifficulty),
                            typeOfRoute = Type.valueOf(selectedType)
                        )
                    )
                    onRouteAdded()
                },
                enabled = name.isNotBlank() && length.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }
        }
    }
}