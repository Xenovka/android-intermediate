package com.dicoding.storyapp.ui.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.databinding.ActivityRegisterBinding
import com.dicoding.storyapp.ui.view.ViewModelFactory
import com.dicoding.storyapp.ui.view.login.Login

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory()
        )[RegisterViewModel::class.java]

        binding.apply {
            viewModel.isSuccessful.observe(this@Register) {
                showMessage(it)
            }

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
        val intent = Intent(this@Register, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun showMessage(state: Boolean) {
        if(state) {
            showLoading(false)
            Toast.makeText(this@Register, "Register Successful", Toast.LENGTH_SHORT).show()
            switchActivity()
        } else {
            Toast.makeText(this@Register, "Invalid credentials! Please check again.", Toast.LENGTH_SHORT).show()
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