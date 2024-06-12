package com.cafstone.application.view.preferance

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cafstone.application.R
import com.cafstone.application.databinding.ActivityPreferanceBinding
import com.cafstone.application.view.ViewModelFactory
import com.cafstone.application.view.main.MainActivity
import com.cafstone.application.view.signup.UserRegisterModel

class PreferenceActivity : AppCompatActivity() {
    private val signupViewModel: PreferanceViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    val fragmentManager = supportFragmentManager
    private lateinit var binding: ActivityPreferanceBinding

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    var servesBeer: Boolean = false
    var servesWine: Boolean = false
    var servesCocktails: Boolean = false
    var goodForChildren: Boolean = false
    var goodForGroups: Boolean = false
    var reservable: Boolean = false
    var outdoorSeating: Boolean = false
    var liveMusic: Boolean = false
    var servesDessert: Boolean = false
    var priceLevel: Int = 0
    var acceptsCreditCards: Boolean = false
    var acceptsDebitCards: Boolean = false
    var acceptsCashOnly: Boolean = false
    var acceptsNfc: Boolean = false

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
            setupView()

            val fragment = fragmentManager.findFragmentByTag(PreferenceFragment1::class.java.simpleName)
            if (fragment !is PreferenceFragment1) {
                Log.d(
                    "MyFlexibleFragment",
                    "Fragment Name :" + PreferenceFragment1::class.java.simpleName
                )
                fragmentManager
                    .beginTransaction()
                    .add(R.id.preferenceViewPager, PreferenceFragment1(), PreferenceFragment1::class.java.simpleName)
                    .commit()
            }
            setupAction()

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
            val fragment = fragmentManager.findFragmentByTag(PreferenceFragment2::class.java.simpleName)
            if (fragment is PreferenceFragment2) {
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.preferenceViewPager, PreferenceFragment1(), PreferenceFragment1::class.java.simpleName)
                    .commit()
                setsubmittext()
            }else{
                finish()
                setsubmittext()
            }
        }
        binding.nextOrSubmitButton.setOnClickListener {
            val fragment = fragmentManager.findFragmentByTag(PreferenceFragment2::class.java.simpleName)
            if (fragment is PreferenceFragment2) {
                val data = UserRegisterModel(
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
            }else{
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.preferenceViewPager, PreferenceFragment2(), PreferenceFragment2::class.java.simpleName)
                    .commit()
                setsubmittext()
            }
        }

        signupViewModel.regist.observe(this) { registerForm ->
            when (registerForm) {
                is PreferanceViewModel.RegistrationStatus.Loading -> {
                }

                is PreferanceViewModel.RegistrationStatus.Success -> {
                    Toast.makeText(this, "Register Successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@PreferenceActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

                is PreferanceViewModel.RegistrationStatus.Error -> {
                    showDialog(registerForm.message)
                }
            }
        }
    }

    fun setsubmittext(){
        val fragment = fragmentManager.findFragmentByTag(PreferenceFragment2::class.java.simpleName)
        if (fragment is PreferenceFragment2) {
            binding.nextOrSubmitButton.text = "Next"
        }else{
            binding.nextOrSubmitButton.text = "Submit"
        }
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Next") { dialog, _ ->
                finish()
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