package com.example.mcqtester.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mcqtester.databinding.ActivitySignUpBinding
import com.example.mcqtester.adminside.Admin
import com.example.mcqtester.studentside.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.signUpBtn.setOnClickListener {
            val email = binding.signUpEmail.text.toString().trim()
            val password = binding.signUpPassword.text.toString().trim()
            val selectedRoleId = binding.signUpRoleGroup.checkedRadioButtonId

            if (email.isEmpty() || password.isEmpty() || selectedRoleId == -1) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val role = when (selectedRoleId) {
                binding.adminRadioBtn.id -> "admin"
                binding.studentRadioBtn.id -> "student"
                else -> null
            }

            if (role == null) {
                Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = auth.currentUser?.uid
                    if (uid == null) {
                        Toast.makeText(this, "UID is null after sign up", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    val userMap = hashMapOf(
                        "email" to email,
                        "role" to role
                    )

                    // Store in separate collections
                    val collectionName = if (role == "admin") "admins" else "students"

                    firestore.collection(collectionName)
                        .document(uid)
                        .set(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show()
                            val nextScreen = if (role == "admin") Admin::class.java else Student::class.java
                            startActivity(Intent(this, nextScreen))
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to save user info: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Sign up failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        binding.goToLogin.setOnClickListener {
            finish() // Go back to Login
        }
    }
}
