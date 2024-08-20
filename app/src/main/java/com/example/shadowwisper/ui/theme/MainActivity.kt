package com.example.shadowwisper.ui.theme


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.syntax_institut.whatssyntax.R
import com.syntax_institut.whatssyntax.databinding.ActivityMainBinding


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
                    // BottomNavigationView auf der Home-Seite ausblenden
                    binding.navBottomBar.visibility = View.GONE
                }
                else -> {
                    // BottomNavigationView auf allen anderen Seiten anzeigen
                    binding.navBottomBar.visibility = View.VISIBLE
                }
            }
        }

// Home Button in der Bottom Navigation manuell behandeln
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