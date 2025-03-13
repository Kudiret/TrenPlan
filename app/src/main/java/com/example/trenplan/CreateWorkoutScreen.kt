package com.example.trenplan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.google.ai.client.generativeai.GenerativeModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateWorkoutScreen(apiKey: String, viewModel: WorkoutViewModel) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    // Список уровней подготовки
    val fitnessLevels = listOf("Новичок", "Средний", "Опытный")
    var selectedFitnessLevel by remember { mutableStateOf(fitnessLevels[0]) }

    // Выбранное количество тренировочных дней
    var trainingDays by remember { mutableStateOf("") }

    var goals by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    var generatedContent by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 1. Рост и вес на одной строке
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Рост (см)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Вес (кг)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Выбор уровня подготовки (три кнопки)
        Text(text = "Уровень подготовки", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            fitnessLevels.forEach { level ->
                OutlinedButton(
                    onClick = { selectedFitnessLevel = level },
                    border = if (selectedFitnessLevel == level)
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    else BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                    shape = MaterialTheme.shapes.small,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = level)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Выбор количества дней в неделю (7 чипов для 1..7)
        Text(text = "Количество дней в неделю", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            (1..7).forEach { day ->
                FilterChip(
                    selected = trainingDays == day.toString(),
                    onClick = { trainingDays = day.toString() },
                    label = { Text(text = day.toString()) },
                    modifier = Modifier.height(48.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // 4. Дополнительные пожелания
        OutlinedTextField(
            value = goals,
            onValueChange = { goals = it },
            label = { Text("Дополнительные пожелания") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Кнопка для генерации программы
        Button(
            onClick = {
                if (height.isNotEmpty() && weight.isNotEmpty() && trainingDays.isNotEmpty()) {
                    isGenerating = true
                    val prompt = "Создай индивидуальную программу тренировок для пользователя с параметрами: " +
                            "Рост: $height см, Вес: $weight кг, Уровень подготовки: $selectedFitnessLevel, " +
                            "Тренировки в неделю: $trainingDays, Цель: $goals."
                    scope.launch {
                        try {
                            val generativeModel = GenerativeModel(
                                modelName = "gemini-1.5-flash",
                                apiKey = apiKey
                            )
                            val response = generativeModel.generateContent(prompt)
                            val generatedText = response.text ?: "Нет данных"
                            generatedContent = generatedText
                            viewModel.addProgram("Новая программа", generatedText)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            isGenerating = false
                        }
                    }
                }
            },
            enabled = !isGenerating,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isGenerating) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Создать программу")
            }
        }

        if (generatedContent.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Сгенерированная программа:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(generatedContent)
        }
    }
}
