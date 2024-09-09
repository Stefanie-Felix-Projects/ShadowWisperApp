package com.example.shadowwisper.ui.theme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.shadowwisper.R
import com.example.shadowwisper.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(binding.navBottomBar, navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.textView.text = "Willkommen Runner"
                    binding.textView.visibility = View.VISIBLE
                    binding.navBottomBar.visibility = View.GONE
                }
                R.id.chatoverviewFragment -> {
                    binding.textView.text = "Chat"
                    binding.textView.visibility = View.VISIBLE
                    binding.navBottomBar.visibility = View.VISIBLE
                }
                R.id.orderoverviewFragment -> {
                    binding.textView.text = "Aufträge"
                    binding.textView.visibility = View.VISIBLE
                    binding.navBottomBar.visibility = View.VISIBLE
                }
                R.id.walletFragment -> {
                    binding.textView.text = "Wallet"
                    binding.textView.visibility = View.VISIBLE
                    binding.navBottomBar.visibility = View.VISIBLE
                }
                R.id.characteroverviewFragment -> {
                    binding.textView.text = "Character"
                    binding.textView.visibility = View.VISIBLE
                    binding.navBottomBar.visibility = View.VISIBLE
                }
                R.id.loginFragment -> {
                    binding.textView.visibility = View.GONE
                    binding.navBottomBar.visibility = View.GONE
                }
                R.id.registerFragment -> {
                    binding.textView.visibility = View.GONE
                    binding.navBottomBar.visibility = View.GONE
                }
                else -> {
                    binding.textView.visibility = View.VISIBLE
                    binding.navBottomBar.visibility = View.VISIBLE
                }
            }
        }

        binding.navBottomBar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.chatoverviewFragment -> {
                    navController.navigate(R.id.chatoverviewFragment)
                    true
                }

                R.id.orderoverviewFragment -> {
                    navController.navigate(R.id.orderoverviewFragment)
                    true
                }

                R.id.walletFragment -> {
                    navController.navigate(R.id.walletFragment)
                    true
                }

                R.id.characteroverviewFragment -> {
                    navController.navigate(R.id.characteroverviewFragment)
                    true
                }

                else -> false
            }
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}