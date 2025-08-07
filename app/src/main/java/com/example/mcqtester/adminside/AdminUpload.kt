package com.example.mcqtester.adminside

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mcqtester.databinding.ActivityAdminUploadBinding

class AdminUpload : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUpload.setOnClickListener {
            val question = binding.etQuestion.text.toString().trim()
            val optionA = binding.etOptionA.text.toString().trim()
            val optionB = binding.etOptionB.text.toString().trim()
            val optionC = binding.etOptionC.text.toString().trim()
            val optionD = binding.etOptionD.text.toString().trim()
            val correctAnswer = binding.etCorrectAnswer.text.toString().trim()

            if (question.isEmpty() || optionA.isEmpty() || optionB.isEmpty() ||
                optionC.isEmpty() || optionD.isEmpty() || correctAnswer.isEmpty()
            ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO: Upload MCQ to Firestore or any storage logic
            Toast.makeText(this, "MCQ uploaded successfully", Toast.LENGTH_SHORT).show()
            binding.etQuestion.text.clear()
            binding.etOptionA.text.clear()
            binding.etOptionB.text.clear()
            binding.etOptionC.text.clear()
            binding.etOptionD.text.clear()
            binding.etCorrectAnswer.text.clear()
        }
    }
}
