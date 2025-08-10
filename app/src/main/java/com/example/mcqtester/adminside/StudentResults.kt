package com.example.mcqtester.adminside

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mcqtester.adminside.adapter.StudentResultAdapter
import com.example.mcqtester.databinding.ActivityStudentResultsBinding
import com.example.mcqtestlab.model.StudentResult
import com.google.firebase.firestore.FirebaseFirestore

class StudentResults : AppCompatActivity() {

    private lateinit var binding: ActivityStudentResultsBinding
    private lateinit var firestore: FirebaseFirestore
    private val resultsList = mutableListOf<StudentResult>()
    private lateinit var adapter: StudentResultAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStudentResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        adapter = StudentResultAdapter(resultsList)
        binding.studentResultsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.studentResultsRecyclerView.adapter = adapter

        fetchResults()
    }

    private fun fetchResults() {
        firestore.collection("results")
            .get()
            .addOnSuccessListener { snapshot ->
                resultsList.clear()
                for (doc in snapshot.documents) {
                    val studentId = doc.getString("studentId") ?: "Unknown"
                    val score = doc.getLong("score")?.toInt() ?: 0
                    val total = doc.getLong("total")?.toInt() ?: 0


                    firestore.collection("users").document(studentId).get()
                        .addOnSuccessListener { userDoc ->
                            val studentName = userDoc.getString("name") ?: "Unknown Student"
                            resultsList.add(StudentResult(studentName, score, total))
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener {
                            resultsList.add(StudentResult("Unknown Student", score, total))
                            adapter.notifyDataSetChanged()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load results: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
