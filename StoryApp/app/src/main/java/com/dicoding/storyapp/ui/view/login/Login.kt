package com.dicoding.storyapp.ui.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.ui.view.ViewModelFactory
import com.dicoding.storyapp.ui.view.main.MainActivity
import com.dicoding.storyapp.ui.view.register.Register

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory()
        )[LoginViewModel::class.java]

        binding.apply {
            btnLogin.setOnClickListener {
                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text.toString()

                viewModel.login(email, password)
                switchActivity("main")
            }

            ctaToRegister.setOnClickListener {
                switchActivity("register")
            }
        }
    }

    private fun switchActivity(dest: String) {
        val intent: Intent = when (dest) {
            "main" -> Intent(this@Login, MainActivity::class.java)
            "register" -> Intent(this@Login, Register::class.java)
            else -> throw IllegalArgumentException("Unknown activity: $dest")
        }

        startActivity(intent)
        finish()
    }
}