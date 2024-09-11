/**
 * Hauptaktivität `MainActivity`, die für die Verwaltung der gesamten Navigation in der App zuständig ist.
 * Diese Aktivität bindet die Navigation über das `NavHostFragment` und den Bottom-Navigation-Balken ein.
 * Die Navigationselemente und die Sichtbarkeit der UI-Elemente (wie der Textanzeige und der Navigationsleiste) werden dynamisch basierend auf dem aktuell angezeigten Fragment verwaltet.
 */
package com.example.shadowwisper.ui.theme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.shadowwisper.R
import com.example.shadowwisper.databinding.ActivityMainBinding
import com.example.shadowwisper.ui.theme.data.model.ChatRoom
import com.example.shadowwisper.ui.theme.data.repository.CharacterDetailRepository

/**
 * `MainActivity` ist die Hauptaktivität, die bei App-Start geladen wird.
 * Sie ist für die gesamte Navigation und die Benutzeroberfläche der App zuständig.
 */
class MainActivity : AppCompatActivity() {

    // Binding-Objekt für das Layout, um UI-Elemente einfach zu referenzieren.
    private lateinit var binding: ActivityMainBinding

    /**
     * Wird beim Erstellen der Aktivität aufgerufen und initialisiert die Benutzeroberfläche und die Navigation.
     * @param savedInstanceState Der gespeicherte Zustand der Aktivität, falls die Aktivität neu erstellt wird.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialisiert das Layout mithilfe von ViewBinding.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialisiert das `NavHostFragment` für die Navigation zwischen den Fragmenten.
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        // Setzt den Bottom-Navigation-Balken mit dem `navController` auf.
        setupWithNavController(binding.navBottomBar, navController)

        /**
         * Fügt einen Listener hinzu, um bei Änderungen des Navigationsziels (Fragmentwechsel) die Sichtbarkeit und den Text der UI-Elemente anzupassen.
         */
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    // Startseite
                    binding.textView.text = "Willkommen Runner"
                    binding.textView.visibility = View.VISIBLE
                    binding.navBottomBar.visibility = View.GONE
                }
                R.id.chatoverviewFragment -> {
                    // Chatübersicht
                    binding.textView.text = "Chat"
                    binding.textView.visibility = View.VISIBLE
                    binding.navBottomBar.visibility = View.VISIBLE
                }
                R.id.orderoverviewFragment -> {
                    // Auftragsübersicht
                    binding.textView.text = "Aufträge"
                    binding.textView.visibility = View.VISIBLE
                    binding.navBottomBar.visibility = View.VISIBLE
                }
                R.id.walletFragment -> {
                    // Wallet-Ansicht
                    binding.textView.text = "Wallet"
                    binding.textView.visibility = View.VISIBLE
                    binding.navBottomBar.visibility = View.VISIBLE
                }
                R.id.characteroverviewFragment -> {
                    // Charakterübersicht
                    binding.textView.text = "Character"
                    binding.textView.visibility = View.VISIBLE
                    binding.navBottomBar.visibility = View.VISIBLE
                }
                R.id.loginFragment, R.id.registerFragment, R.id.ordercompletionFragment -> {
                    // Login-, Registrierungs- und Auftragsabschluss-Ansicht: Navigation und Text ausblenden.
                    binding.textView.visibility = View.GONE
                    binding.navBottomBar.visibility = View.GONE
                }
                else -> {
                    // Standardansicht: Text und Navigationsleiste anzeigen.
                    binding.textView.visibility = View.VISIBLE
                    binding.navBottomBar.visibility = View.VISIBLE
                }
            }
        }

        /**
         * Setzt einen Klicklistener für die Elemente der Bottom-Navigationsleiste, um zwischen den Fragmenten zu navigieren.
         */
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

    /**
     * Handhabt das Navigieren nach oben, wenn der Benutzer die Navigationspfeile verwendet.
     * @return `true`, wenn die Navigation erfolgreich war, sonst der Standardwert von `super.onSupportNavigateUp()`.
     */
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}