package com.example.trenplan

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HistoryScreen(viewModel: WorkoutViewModel) {
    val programs = viewModel.workoutPrograms
    var selectedProgram by remember { mutableStateOf<WorkoutProgram?>(null) }
    var editingName by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        if (programs.isEmpty()) {
            Text("История пуста")
        } else {
            LazyColumn {
                items(programs) { program ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            OutlinedTextField(
                                value = if (selectedProgram?.id == program.id) editingName else program.name,
                                onValueChange = { editingName = it },
                                label = { Text("Название программы") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Содержание:", style = MaterialTheme.typography.bodyMedium)
                            Text(program.content)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                if (selectedProgram?.id == program.id) {
                                    TextButton(onClick = {
                                        viewModel.updateProgramName(program.id, editingName)
                                        selectedProgram = null
                                    }) {
                                        Text("Сохранить")
                                    }
                                } else {
                                    TextButton(onClick = {
                                        selectedProgram = program
                                        editingName = program.name
                                    }) {
                                        Text("Редактировать")
                                    }
                                }
                                TextButton(onClick = {
                                    viewModel.deleteProgram(program.id)
                                }) {
                                    Text("Удалить")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
