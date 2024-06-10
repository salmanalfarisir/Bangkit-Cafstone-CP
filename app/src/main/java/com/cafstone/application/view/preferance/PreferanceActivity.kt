package com.cafstone.application.view.preferance

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.cafstone.application.databinding.ActivityPreferanceBinding
import com.cafstone.application.view.ViewModelFactory
import com.cafstone.application.view.signup.UserRegisterModel

class PreferanceActivity : AppCompatActivity() {
    private val signupViewModel: PreferanceViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding : ActivityPreferanceBinding
    private lateinit var data : UserRegisterModel
    private lateinit var name : String
    private lateinit var email : String
    private lateinit var password : String
    var servesBeer : Boolean = false
    var servesWine : Boolean = false
    var servesCocktails : Boolean = false
    var goodForChildren : Boolean = false
    var goodForGroups : Boolean = false
    var reservable : Boolean = false
    var outdoorSeating : Boolean = false
    var liveMusic : Boolean = false
    var servesDessert : Boolean = false
    var priceLevel : Int = 0
    var acceptsCreditCards : Boolean = false
    var acceptsDebitCards: Boolean = false
    var acceptsCashOnly: Boolean = false
    var acceptsNfc : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val nam = intent.getStringExtra(NAME)
        val emai = intent.getStringExtra(EMAIL)
        val passwor = intent.getStringExtra(PASSWORD)
        if (nam!=null && emai!=null && passwor!=null)
        {
            name = nam
            email = emai
            password = passwor
            data = UserRegisterModel(name,email,password,servesBeer,servesWine,servesCocktails,goodForChildren,goodForGroups,reservable,outdoorSeating,liveMusic,servesDessert,priceLevel,acceptsCreditCards,acceptsDebitCards,acceptsCashOnly,acceptsNfc)

            binding.submit.setOnClickListener {
                data = UserRegisterModel(name,email,password,servesBeer,servesWine,servesCocktails,goodForChildren,goodForGroups,reservable,outdoorSeating,liveMusic,servesDessert,priceLevel,acceptsCreditCards,acceptsDebitCards,acceptsCashOnly,acceptsNfc)
                signupViewModel.register(data)
            }
            signupViewModel.regist.observe(this) { regist ->
                when (regist) {
                    is PreferanceViewModel.RegistrationStatus.Loading -> {
                    }

                    is PreferanceViewModel.RegistrationStatus.Success -> {
                        Toast.makeText(this,"Register Sucesfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }

                    is PreferanceViewModel.RegistrationStatus.Error -> {
                        showDialog(regist.message)
                    }
                }
            }
        }else{
            finish()
        }
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }
    companion object{
        const val NAME = "name"
        const val EMAIL = "email"
        const val PASSWORD = "password"
    }
}