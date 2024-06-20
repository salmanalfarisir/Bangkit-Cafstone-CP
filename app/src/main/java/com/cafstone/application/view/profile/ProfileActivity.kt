package com.cafstone.application.view.profile

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cafstone.application.R
import com.cafstone.application.databinding.ActivityProfileBinding
import com.cafstone.application.view.ViewModelFactory
import com.cafstone.application.view.main.MainActivity

@Suppress("DEPRECATION")
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileToolbar.title.text = "Profile"
        binding.profileToolbar.title.setTextColor(Color.WHITE)
        binding.profileToolbar.toolbar.setBackgroundColor(Color.TRANSPARENT)

        binding.profileToolbar.backButton.visibility = View.GONE

        binding.profileToolbar.toolbar.apply {
            setSupportActionBar(this)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.charm__arrow_left_white)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = ""
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        }

        val data = viewModel.getData()
        binding.profileName.text = data.name
        binding.profileEmail.text = data.email
        Glide.with(this)
            .load("https://cdn.idntimes.com/content-images/post/20240207/33bac083ba44f180c1435fc41975bf36-ca73ec342155d955387493c4eb78c8bb.jpg")
            .into(binding.profileImage)

        binding.changePassword.setOnClickListener {
            startActivity(Intent(this, ProfilePasswordActivity::class.java))
        }
        binding.logout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(this@ProfileActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}