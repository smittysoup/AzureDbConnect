package com.example.azuredbconnect

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val submitButton = findViewById<Button>(R.id.button_get_help)
        val problemAskEditText = findViewById<EditText>(R.id.problem_ask)
        val helpDescriptionEditText = findViewById<EditText>(R.id.help_description)


        submitButton.setOnClickListener {
            val problemText = problemAskEditText.text.toString()
            val problemDescription = helpDescriptionEditText.text.toString()

            // Call Azure Function
            val tableName = "problem"
            val params = mapOf(
                "col1_name" to "name",
                "col1_value" to problemText,
                "col2_name" to "description",
                "col2_value" to problemDescription,
                "col3_name" to "fk_student_id",
                "col3_value" to "1"
            )
            println(tableName + params)
            lifecycleScope.launch {
                try {
                    val response = callAzureFunction(tableName, params)
                    println("Azure Function response: $response")
                } catch (e: Exception) {
                    println("Error calling Azure Function: ${e.message}")
                }
            }
        }
    }
}



