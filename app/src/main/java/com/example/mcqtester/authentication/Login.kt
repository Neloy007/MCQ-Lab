package com.example.mcqtester.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mcqtester.adminside.Admin
import com.example.mcqtester.databinding.ActivityLoginBinding
import com.example.mcqtester.studentside.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.loginBtn.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = auth.currentUser?.uid
                    if (uid == null) {
                        Toast.makeText(this, "Login failed: UID not found", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    firestore.collection("users").document(uid).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                val role = document.getString("role")
                                when (role) {
                                    "admin" -> {
                                        startActivity(Intent(this, Admin::class.java))
                                        finish()
                                    }
                                    "student" -> {
                                        startActivity(Intent(this, Student::class.java))
                                        finish()
                                    }
                                    else -> {
                                        Toast.makeText(this, "Role not found for user", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(this, "User record not found in Firestore", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener { e ->
                            Log.e("FirestoreError", "Error fetching document", e)
                            Toast.makeText(this, "Error fetching user data: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        binding.goToSignUp.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }
}
