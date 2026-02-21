package com.example.theguide

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.theguide.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        val navController = navHost.navController

        // Home/Favorites otomatik
        binding.bottomNav.setupWithNavController(navController)

        // Back item özel
        binding.bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.backAction) {
                onBackPressedDispatcher.onBackPressed()
                true
            } else {
                NavigationUI.onNavDestinationSelected(item, navController)
            }
        }
    }
}