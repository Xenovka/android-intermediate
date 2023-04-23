package com.dicoding.storyapp.ui.view.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
            btnRegister.setOnClickListener {
                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()

                viewModel.register(name, email, password)
                val intent = Intent(this@Register, Login::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}