package com.example.mcqtester.studentside

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mcqtester.authentication.Login
import com.example.mcqtester.authentication.MainActivity
import com.example.mcqtester.authentication.SignUp
import com.example.mcqtester.databinding.ActivityStudentBinding
import com.example.mcqtestlab.adapter.McqAdapter
import com.example.mcqtestlab.model.MCQ
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Student : AppCompatActivity() {

    private lateinit var binding: ActivityStudentBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: McqAdapter

    private val mcqList = mutableListOf<MCQ>()
    private val selectedAnswers = mutableMapOf<Int, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupRecyclerView()
        fetchQuestions()

        binding.backToLoginBtn.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        binding.submitAnswersBtn.setOnClickListener {
            if (mcqList.isEmpty()) {
                Toast.makeText(this, "No questions available", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Calculate score by counting how many answers match the correct answer index
            val score = mcqList.indices.count { i ->
                mcqList[i].correctAnswerIndex == selectedAnswers[i]
            }

            val studentId = auth.currentUser?.uid ?: return@setOnClickListener

            val resultData = hashMapOf(
                "studentId" to studentId,
                "score" to score,
                "total" to mcqList.size
            )

            // Save result to Firestore
            db.collection("results").add(resultData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Submitted! Your score: $score/${mcqList.size}", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to submit answers: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setupRecyclerView() {
        adapter = McqAdapter(mcqList) { questionIndex, selectedOptionIndex ->
            selectedAnswers[questionIndex] = selectedOptionIndex
        }

        binding.studentMcqRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.studentMcqRecycler.adapter = adapter
    }

    private fun fetchQuestions() {
        db.collection("mcqs").get()
            .addOnSuccessListener { snapshot ->
                mcqList.clear()
                for (doc in snapshot.documents) {
                    val mcq = doc.toObject(MCQ::class.java)
                    mcq?.let { mcqList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load questions: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
