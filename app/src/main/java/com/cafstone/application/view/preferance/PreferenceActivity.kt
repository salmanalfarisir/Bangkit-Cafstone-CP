package com.cafstone.application.view.preferance

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cafstone.application.databinding.ActivityPreferanceBinding
import com.cafstone.application.view.ViewModelFactory
import com.cafstone.application.view.signup.SignupActivity
import com.cafstone.application.view.signup.UserRegisterModel

class PreferenceActivity : AppCompatActivity() {
    private val signupViewModel: PreferanceViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityPreferanceBinding
    private lateinit var data: UserRegisterModel

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    private var servesBeer: Boolean = false
    private var servesWine: Boolean = false
    private var servesCocktails: Boolean = false
    private var goodForChildren: Boolean = false
    private var goodForGroups: Boolean = false
    private var reservable: Boolean = false
    private var outdoorSeating: Boolean = false
    private var liveMusic: Boolean = false
    private var servesDessert: Boolean = false
    private var priceLevel: Int = 0
    private var acceptsCreditCards: Boolean = false
    private var acceptsDebitCards: Boolean = false
    private var acceptsCashOnly: Boolean = false
    private var acceptsNfc: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()

        // Variable Register User Preference
        val registerName = intent.getStringExtra(NAME)
        val registerEmail = intent.getStringExtra(EMAIL)
        val registerPassword = intent.getStringExtra(PASSWORD)

        if (registerName != null && registerEmail != null && registerPassword != null) {
            name = registerName
            email = registerEmail
            password = registerPassword
            data = UserRegisterModel(
                name,
                email,
                password,
                servesBeer,
                servesWine,
                servesCocktails,
                goodForChildren,
                goodForGroups,
                reservable,
                outdoorSeating,
                liveMusic,
                servesDessert,
                priceLevel,
                acceptsCreditCards,
                acceptsDebitCards,
                acceptsCashOnly,
                acceptsNfc
            )

            binding.nextOrSubmitButton.setOnClickListener {
                data = UserRegisterModel(
                    name,
                    email,
                    password,
                    servesBeer,
                    servesWine,
                    servesCocktails,
                    goodForChildren,
                    goodForGroups,
                    reservable,
                    outdoorSeating,
                    liveMusic,
                    servesDessert,
                    priceLevel,
                    acceptsCreditCards,
                    acceptsDebitCards,
                    acceptsCashOnly,
                    acceptsNfc
                )
                signupViewModel.register(data)
            }
            signupViewModel.regist.observe(this) { registerForm ->
                when (registerForm) {
                    is PreferanceViewModel.RegistrationStatus.Loading -> {
                    }

                    is PreferanceViewModel.RegistrationStatus.Success -> {
                        Toast.makeText(this, "Register Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    is PreferanceViewModel.RegistrationStatus.Error -> {
                        showDialog(registerForm.message)
                    }
                }
            }
        } else {
            finish()
        }
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars())
        }
    }

    private fun setupAction() {
        binding.backButton.setOnClickListener {
            startActivity(Intent(this@PreferenceActivity, SignupActivity::class.java))
        }
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

    companion object {
        const val NAME = "name"
        const val EMAIL = "email"
        const val PASSWORD = "password"
    }
}