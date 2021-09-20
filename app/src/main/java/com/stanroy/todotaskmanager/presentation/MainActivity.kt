package com.stanroy.todotaskmanager.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.stanroy.todotaskmanager.R
import com.stanroy.todotaskmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.list_menu, menu)
//        return true
//    }
}