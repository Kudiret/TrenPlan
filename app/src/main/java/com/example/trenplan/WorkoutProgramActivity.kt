package com.example.trenplan

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.trenplan.R

class WorkoutProgramActivity : AppCompatActivity() {

    private lateinit var workoutProgramText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_program)

        workoutProgramText = findViewById(R.id.workoutProgramText)
        val workoutProgram = intent.getStringExtra("workoutProgram")
        workoutProgramText.setText(workoutProgram)
    }
}
