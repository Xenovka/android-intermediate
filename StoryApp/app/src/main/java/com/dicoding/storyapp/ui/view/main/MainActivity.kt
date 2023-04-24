package com.dicoding.storyapp.ui.view.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.model.UserPreference
import com.dicoding.storyapp.ui.view.ViewModelFactory
import com.dicoding.storyapp.ui.view.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        viewModel.getUser().observe(this) {user ->
            if(user.isLoggedIn) {
                binding.tvHomeUsername.text = getString(R.string.greeting, user.name)
            } else {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}