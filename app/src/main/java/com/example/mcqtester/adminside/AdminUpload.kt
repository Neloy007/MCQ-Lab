package com.example.mcqtester.adminside

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mcqtester.databinding.ActivityAdminUploadBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class AdminUpload : AppCompatActivity() {

    private lateinit var binding: ActivityAdminUploadBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Fetch and display admin info
        fetchAdminInfo()

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

            val mcqData = hashMapOf(
                "question" to question,
                "optionA" to optionA,
                "optionB" to optionB,
                "optionC" to optionC,
                "optionD" to optionD,
                "correctAnswer" to correctAnswer,
                "uploadedBy" to auth.currentUser?.uid,
                "timestamp" to System.currentTimeMillis()
            )

            firestore.collection("mcqs")
                .add(mcqData)
                .addOnSuccessListener {
                    Toast.makeText(this, "MCQ uploaded successfully", Toast.LENGTH_SHORT).show()
                    clearFields()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun fetchAdminInfo() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name") ?: "Admin Name"
                    val email = document.getString("email") ?: "admin@example.com"

                    binding.tvAdminName.text = name
                    binding.tvAdminEmail.text = email
                } else {
                    Toast.makeText(this, "Admin info not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch admin info: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        binding.etQuestion.text.clear()
        binding.etOptionA.text.clear()
        binding.etOptionB.text.clear()
        binding.etOptionC.text.clear()
        binding.etOptionD.text.clear()
        binding.etCorrectAnswer.text.clear()
    }
}
