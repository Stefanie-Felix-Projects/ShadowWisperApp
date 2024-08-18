package com.example.shadowwisper.ui.theme

import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
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

        // Listener, für die Sichtbarkeit der BottomNavigationView zu steuern
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val r = Rect()
                binding.root.getWindowVisibleDisplayFrame(r)
                val screenHeight = binding.root.rootView.height
                val keypadHeight = screenHeight - r.bottom

                if (keypadHeight > screenHeight * 0.15) {
                    //Tastatur sichtbar
                    binding.navBottomBar.visibility = View.GONE
                } else {
                    // Tastatur unsichtbar
                    binding.navBottomBar.visibility = View.VISIBLE
                }
            }

        })
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}