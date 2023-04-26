package com.dicoding.storyapp.ui.view.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.model.UserModel
import com.dicoding.storyapp.model.UserPreference
import com.dicoding.storyapp.ui.view.ViewModelFactory
import com.dicoding.storyapp.ui.view.main.MainActivity
import com.dicoding.storyapp.ui.view.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()

        binding.apply {
            btnLogin.setOnClickListener {
                showLoading(true)
                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text.toString()

                viewModel.login(email, password)
            }

            ctaToRegister.setOnClickListener {
                switchActivity("register")
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        viewModel.isSuccessful.observe(this@LoginActivity) {
            setupAction(it)
        }

        viewModel.getUser().observe(this) { user ->
            this.user = user
        }
    }

    private fun switchActivity(dest: String) {
        val intent: Intent = when (dest) {
            "main" -> Intent(this@LoginActivity, MainActivity::class.java)
            "register" -> Intent(this@LoginActivity, RegisterActivity::class.java)
            else -> throw IllegalArgumentException("Unknown activity: $dest")
        }

        startActivity(intent)
        finish()
    }

    private fun setupAction(state: Boolean) {
        if(state) {
            showLoading(false)
            switchActivity("main")
        } else {
            Toast.makeText(this@LoginActivity, "email or password is incorrect!", Toast.LENGTH_SHORT).show()
            showLoading(false)
        }
    }

    private fun showLoading(state: Boolean) {
        if(state) {
            binding.progressLogin.visibility = View.VISIBLE
        } else {
            binding.progressLogin.visibility = View.GONE
        }
    }
}