package com.dicoding.storyapp.ui.view.register

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.model.UserPreference
import com.dicoding.storyapp.ui.view.ViewModelFactory
import com.dicoding.storyapp.ui.view.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewModel()

        binding.apply {
            btnRegister.setOnClickListener {
                showLoading(true)

                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()

                viewModel.register(name, email, password)
            }

            ctaToLogin.setOnClickListener {
                switchActivity()
            }
        }
    }

    private fun switchActivity() {
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[RegisterViewModel::class.java]

        viewModel.isSuccessful.observe(this@RegisterActivity) {
            showMessage(it)
        }
    }

    private fun showMessage(state: Boolean) {
        if(state) {
            showLoading(false)
            Toast.makeText(this@RegisterActivity, "Register Successful", Toast.LENGTH_SHORT).show()
            switchActivity()
        } else {
            Toast.makeText(this@RegisterActivity, "Invalid credentials! Please check again.", Toast.LENGTH_SHORT).show()
            showLoading(false)
        }
    }

    private fun showLoading(state: Boolean) {
        if(state) {
            binding.progressRegister.visibility = View.VISIBLE
        } else {
            binding.progressRegister.visibility = View.GONE
        }
    }
}