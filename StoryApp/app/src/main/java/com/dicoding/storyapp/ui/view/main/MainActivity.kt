package com.dicoding.storyapp.ui.view.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.adapter.StoryListAdapter
import com.dicoding.storyapp.api.StoryItem
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.model.UserPreference
import com.dicoding.storyapp.ui.view.ViewModelFactory
import com.dicoding.storyapp.ui.view.addStory.AddStoryActivity
import com.dicoding.storyapp.ui.view.detail.DetailActivity
import com.dicoding.storyapp.ui.view.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var storyListAdapter: StoryListAdapter
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(true)

        setupViewModel()
        setupRecyclerView()

        playAnimation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_logout -> {
                viewModel.logout()
                true
            }
            R.id.menu_change_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.menu_add_story -> {
                startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        viewModel.getUser().observe(this) {user ->
            if(!user.isLoggedIn) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {
                token = user.token
                viewModel.setAllStories(token)
            }
        }

        viewModel.getAllStories().observe(this) {
            if(it != null) {
                storyListAdapter.setStories(it)
                showLoading(false)
            }
        }
    }

    private fun setupRecyclerView() {
        storyListAdapter = StoryListAdapter()

        binding.apply {
            rvStory.layoutManager = LinearLayoutManager(this@MainActivity)
            rvStory.setHasFixedSize(true)
            rvStory.adapter = storyListAdapter
        }

        storyListAdapter.setOnItemClickCallback(object: StoryListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: StoryItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ID, data.id)
                startActivity(intent)
            }
        })
    }

    private fun playAnimation() {
        val storyAnimation = ObjectAnimator.ofFloat(binding.rvStory, View.ALPHA, 1f).setDuration(1000)
        AnimatorSet().apply {
            play(storyAnimation)
            start()
        }
    }

    private fun showLoading(state: Boolean) {
        if(state) {
            binding.progressStory.visibility = View.VISIBLE
        } else {
            binding.progressStory.visibility = View.GONE
        }
    }
}