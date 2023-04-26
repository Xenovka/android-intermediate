package com.dicoding.storyapp.ui.view.detail

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.ActivityDetailBinding
import com.dicoding.storyapp.model.UserPreference
import com.dicoding.storyapp.ui.view.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var token: String? = null

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        showLoading(true)

        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[DetailViewModel::class.java]

        val userId = intent.getStringExtra(EXTRA_ID)

        viewModel.getUser().observe(this) {
            if(it.token?.isNotEmpty() == true) {
                token = it.token
                viewModel.setStoryDetail(userId, token)
            }
        }

        viewModel.getStoryDetail().observe(this) {
            if(it != null) {
                binding.apply {
                    tvDetailName.text = it.name
                    tvDetailDescription.text = it.description

                    Glide.with(this@DetailActivity)
                        .load(it.photoUrl)
                        .centerCrop()
                        .into(ivDetailPhoto)
                }

                showLoading(false)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if(state) {
            binding.progressDetailStory.visibility = View.VISIBLE
        } else {
            binding.progressDetailStory.visibility = View.GONE
        }
    }
}