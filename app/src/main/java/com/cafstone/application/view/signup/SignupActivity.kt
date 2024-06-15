package com.cafstone.application.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.cafstone.application.databinding.ActivitySignupBinding
import com.cafstone.application.view.preferance.PreferenceActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        showLoading(false)
        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION") if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    @Suppress("SameParameterValue")
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setButtonEnabled() {
        binding.signupButton.isEnabled = binding.nameEditText.text.toString()
            .isNotEmpty() && (binding.emailEditText.text.toString()
            .isNotEmpty() && binding.emailEditTextLayout.error == null) && (binding.passwordEditText.text.toString()
            .isNotEmpty() && binding.passwordEditTextLayout.error == null) && (binding.passwordConfirmEditText.text.toString()
            .isNotEmpty() && binding.passwordConfirmEditTextLayout.error == null)
    }

    private fun setupAction() {
        binding.passwordEditText.setErrorTextView(binding.passwordEditTextLayout)
        binding.passwordEditText.setPasswordTextView(binding.passwordConfirmEditText)
        binding.passwordEditText.setPasswordErrorTextView(binding.passwordConfirmEditTextLayout)
        binding.emailEditText.setErrorTextView(binding.emailEditTextLayout)
        binding.passwordConfirmEditText.setTextView(binding.passwordConfirmEditText)
        binding.passwordConfirmEditText.setErrorTextView(binding.passwordConfirmEditTextLayout)
        binding.passwordConfirmEditText.setPasswordTextView(binding.passwordEditText)
        setButtonEnabled()
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed
                setButtonEnabled()
            }
        }
        binding.nameEditText.addTextChangedListener(textWatcher)
        binding.emailEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)
        binding.passwordConfirmEditText.addTextChangedListener(textWatcher)

        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val nama = binding.nameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isNotEmpty() && nama.isNotEmpty() && password.isNotEmpty()) {
                if (binding.emailEditTextLayout.error == null && binding.passwordEditTextLayout.error == null) {
                    val intent = Intent(this, PreferenceActivity::class.java)
                    intent.putExtra(PreferenceActivity.NAME, nama)
                    intent.putExtra(PreferenceActivity.EMAIL, email)
                    intent.putExtra(PreferenceActivity.PASSWORD, password)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Error Email or Password", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Fill the form", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.title, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.subTitle, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordConfirmTextView =
            ObjectAnimator.ofFloat(binding.passwordConfirmTextView, View.ALPHA, 1f).setDuration(100)
        val passwordConfirmEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordConfirmEditTextLayout, View.ALPHA, 1f)
                .setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                passwordConfirmTextView,
                passwordConfirmEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }

}