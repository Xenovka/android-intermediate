package com.dicoding.storyapp.ui.view.register

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
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.isValidEmail
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

        playAnimation()
        setupViewModel()

        binding.apply {
            setupValidateInput()

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

    private fun setupValidateInput() {
        binding.edRegisterName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { setButtonEnabled() }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edRegisterEmail.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { setButtonEnabled() }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edRegisterPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { setButtonEnabled() }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    private fun setButtonEnabled() {
        val name = binding.edRegisterName.text
        val email = binding.edRegisterEmail.text
        val password = binding.edRegisterPassword.text
        binding.btnRegister.isEnabled = name.toString().isNotEmpty() && email.isValidEmail() && password.toString().length >= 8
    }

    private fun playAnimation() {
        binding.apply {
            val name = ObjectAnimator.ofFloat(registerNameLayout, View.ALPHA, 1f).setDuration(500)
            val email = ObjectAnimator.ofFloat(registerEmailLayout, View.ALPHA, 1f).setDuration(500)
            val password = ObjectAnimator.ofFloat(registerPasswordLayout, View.ALPHA, 1f).setDuration(500)
            val ctaLogin = ObjectAnimator.ofFloat(ctaToLogin, View.ALPHA, 1f).setDuration(500)
            val btnRegister = ObjectAnimator.ofFloat(btnRegister, View.ALPHA, 1f).setDuration(500)

            val together = AnimatorSet().apply {
                playTogether(ctaLogin, btnRegister)
            }

            AnimatorSet().apply {
                playSequentially(name, email, password, together)
                start()
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