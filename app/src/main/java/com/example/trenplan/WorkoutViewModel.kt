package com.example.trenplan

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class WorkoutProgram(
    val id: Int,
    var name: String,
    val content: String
)

class WorkoutViewModel : ViewModel() {
    private var nextId = 0
    // Список программ хранится как состояние Compose
    val workoutPrograms = mutableStateListOf<WorkoutProgram>()

    fun addProgram(name: String, content: String) {
        workoutPrograms.add(WorkoutProgram(nextId++, name, content))
    }

    fun updateProgramName(id: Int, newName: String) {
        val index = workoutPrograms.indexOfFirst { it.id == id }
        if (index != -1) {
            workoutPrograms[index] = workoutPrograms[index].copy(name = newName)
        }
    }

    fun deleteProgram(id: Int) {
        workoutPrograms.removeAll { it.id == id }
    }
}
