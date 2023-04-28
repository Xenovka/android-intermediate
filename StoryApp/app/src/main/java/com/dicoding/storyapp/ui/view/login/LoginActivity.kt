package com.dicoding.storyapp.ui.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.isValidEmail
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

        playAnimation()
        setupViewModel()

        binding.apply {
            setupValidateInput()

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

    private fun setupValidateInput() {
        binding.edLoginEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnabled()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edLoginPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonEnabled()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun setButtonEnabled() {
        val email = binding.edLoginEmail.text
        val password = binding.edLoginPassword.text
        binding.btnLogin.isEnabled = email.isValidEmail() && password.toString().length >= 8
    }

    private fun playAnimation() {
        binding.apply {
            val email = ObjectAnimator.ofFloat(loginEmailLayout, View.ALPHA, 1f).setDuration(500)
            val password = ObjectAnimator.ofFloat(loginPasswordLayout, View.ALPHA, 1f).setDuration(500)
            val ctaToRegister = ObjectAnimator.ofFloat(ctaToRegister, View.ALPHA, 1f).setDuration(500)
            val btnLogin = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(500)

            val together = AnimatorSet().apply {
                playTogether(ctaToRegister, btnLogin)
            }

            AnimatorSet().apply {
                playSequentially(email, password, together)
                start()
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

    private fun showLoading(state: Boolean) { binding.progressLogin.visibility = if (state) View.VISIBLE else View.GONE }
}