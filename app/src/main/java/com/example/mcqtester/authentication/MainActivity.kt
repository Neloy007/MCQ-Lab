package com.example.mcqtester.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mcqtester.adminside.Admin
import com.example.mcqtester.studentside.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val user = auth.currentUser
        if (user != null) {
            firestore.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    val role = document.getString("role")
                    when (role) {
                        "admin" -> startActivity(Intent(this, Admin::class.java))
                        "student" -> startActivity(Intent(this, Student::class.java))
                        else -> startActivity(Intent(this, Login::class.java))
                    }
                    finish()
                }.addOnFailureListener {
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
        } else {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}