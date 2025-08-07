package com.example.mcqtester.adminside

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mcqtester.databinding.ActivityAdminBinding

class Admin : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigate to AdminUpload Activity when upload button clicked
        binding.btnUploadMcqs.setOnClickListener {
            startActivity(Intent(this, AdminUpload::class.java))
        }

        // Navigate to AdminViewSubmissions Activity when view submissions clicked
        binding.btnViewSubmissions.setOnClickListener {
            startActivity(Intent(this, StudentResults::class.java))
        }
    }
}
