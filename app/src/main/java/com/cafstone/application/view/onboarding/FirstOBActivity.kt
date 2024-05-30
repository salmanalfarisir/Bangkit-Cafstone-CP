package com.cafstone.application.view.onboarding

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.cafstone.application.databinding.ActivityFirstObactivityBinding

class FirstOBActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFirstObactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstObactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()


        setupView()
        setupAction()
    }

    private fun setupView() {

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.nextButton.setOnClickListener {
            val intent = Intent(this@FirstOBActivity, SecondOBActivity::class.java)
            startActivity(intent)
        }
    }
}