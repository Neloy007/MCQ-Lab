package com.example.mcqtester.adminside

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mcqtester.authentication.Login
import com.example.mcqtester.databinding.ActivityAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Admin : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        fetchAdminDetails()

        binding.btnUploadMcqs.setOnClickListener {
            startActivity(Intent(this, AdminUpload::class.java))
        }

        binding.btnViewSubmissions.setOnClickListener {
            startActivity(Intent(this, StudentResults::class.java))
        }

        binding.goToLoginBtn.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
    }

    private fun fetchAdminDetails() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name") ?: "No Name"
                        val email = document.getString("email") ?: "No Email"

                        binding.tvAdminName.text = name
                        binding.tvAdminEmail.text = email
                    } else {
                        Toast.makeText(this, "Admin record not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error fetching admin data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
