package com.cafstone.application.view.profile

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cafstone.application.databinding.ActivityProfilePasswordBinding
import com.cafstone.application.view.ViewModelFactory

@Suppress("DEPRECATION", "NAME_SHADOWING")
class ProfilePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfilePasswordBinding
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showloading(false)
        binding.profileToolbar.title.text = "Change Password"
        binding.profileToolbar.toolbar.setBackgroundColor(Color.TRANSPARENT)
        binding.profileToolbar.buttonToolbar.visibility = View.GONE
        binding.profileToolbar.toolbar.apply {
            setSupportActionBar(this)
            supportActionBar?.title = ""
        }

        binding.profileToolbar.backButton.setOnClickListener {
            finish()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        }
        binding.oldpasswordEditText.setErrorTextView(binding.oldpasswordEditTextLayout)
        binding.passwordEditText.setErrorTextView(binding.passwordEditTextLayout)
        binding.passwordEditText.setPasswordTextView(binding.passwordConfirmEditText)
        binding.passwordEditText.setPasswordErrorTextView(
            binding.passwordConfirmEditTextLayout,
            "Password Confirm"
        )
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
        binding.oldpasswordEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)
        binding.passwordConfirmEditText.addTextChangedListener(textWatcher)

        binding.signupButton.setOnClickListener {
            val isOldPasswordValid = binding.oldpasswordEditText.text.toString()
                .isNotEmpty() && binding.oldpasswordEditTextLayout.error == null
            val isPasswordValid = binding.passwordEditText.text.toString()
                .isNotEmpty() && binding.passwordEditTextLayout.error == null
            val isPasswordConfirmValid = binding.passwordConfirmEditText.text.toString()
                .isNotEmpty() && binding.passwordConfirmEditTextLayout.error == null
            if (isPasswordValid && isOldPasswordValid && isPasswordConfirmValid) {
                viewModel.password(
                    binding.oldpasswordEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
                viewModel.isLoading.observe(this) { it ->
                    showloading(it)
                    if (!it) {
                        viewModel.password.observe(this) {
                            if (it.success) {
                                Toast.makeText(
                                    this,
                                    "Password kamu berhasil diubah",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                finish()
                            } else {
                                showDialog(it.message)
                            }
                        }
                    }
                }
            } else {
                showDialog("Semua Form Belum Terisi")
            }
        }
    }

    private fun setButtonEnabled() {
        val isOldPasswordValid = binding.oldpasswordEditText.text.toString()
            .isNotEmpty() && binding.oldpasswordEditTextLayout.error == null
        val isPasswordValid = binding.passwordEditText.text.toString()
            .isNotEmpty() && binding.passwordEditTextLayout.error == null
        val isPasswordConfirmValid = binding.passwordConfirmEditText.text.toString()
            .isNotEmpty() && binding.passwordConfirmEditTextLayout.error == null
        binding.signupButton.isEnabled =
            isOldPasswordValid && isPasswordValid && isPasswordConfirmValid
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Next") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun showloading(status: Boolean) {
        if (status) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}