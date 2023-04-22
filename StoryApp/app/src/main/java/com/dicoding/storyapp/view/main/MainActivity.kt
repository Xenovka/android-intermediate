package com.dicoding.storyapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.storyapp.R
import com.dicoding.storyapp.view.login.Login

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this@MainActivity, Login::class.java)
        startActivity(intent)
    }
}