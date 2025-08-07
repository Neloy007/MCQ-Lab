package com.example.mcqtester.adminside

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mcqtester.databinding.ActivityStudentResultsBinding

class StudentResults : AppCompatActivity() {

    private lateinit var binding: ActivityStudentResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStudentResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example usage
        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 10)

        binding.scoreText.text = "$score / $total"
    }
}
