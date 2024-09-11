/**
 * ViewModel-Klasse `LoginViewModel`, die für die Verwaltung des Login-Prozesses zuständig ist.
 * Diese Klasse verwendet Firebase Authentication, um Benutzer mit E-Mail und Passwort anzumelden.
 * Sie bietet LiveData-Objekte zur Beobachtung des Anmeldeerfolgs oder von Fehlern während des Login-Vorgangs.
 */
package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * ViewModel zur Verwaltung des Login-Vorgangs über Firebase Authentication.
 * Es stellt zwei LiveData-Objekte bereit: Eines für den Login-Erfolg und eines für mögliche Fehlernachrichten.
 */
class LoginViewModel : ViewModel() {

    // Privates MutableLiveData, das den Login-Erfolg speichert.
    private val _loginSuccess = MutableLiveData<Boolean>()

    /**
     * Öffentliches LiveData-Objekt, das von der UI beobachtet werden kann.
     * Es gibt `true` zurück, wenn der Login erfolgreich war, und `false`, wenn nicht.
     */
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    // Privates MutableLiveData, das Fehlermeldungen während des Login-Vorgangs speichert.
    private val _loginError = MutableLiveData<String>()

    /**
     * Öffentliches LiveData-Objekt, das von der UI beobachtet werden kann.
     * Es enthält eine Fehlermeldung, falls der Login fehlgeschlagen ist.
     */
    val loginError: LiveData<String> = _loginError

    // Firebase Authentication-Instanz zur Authentifizierung von Benutzern.
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Führt den Login-Vorgang mit E-Mail und Passwort durch.
     * Wenn die Anmeldung erfolgreich ist, wird `loginSuccess` auf `true` gesetzt.
     * Wenn der Login fehlschlägt, wird `loginError` mit der Fehlermeldung des Fehlers befüllt.
     * @param email Die E-Mail-Adresse des Benutzers.
     * @param password Das Passwort des Benutzers.
     */
    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _loginSuccess.value = true // Login erfolgreich
                } else {
                    _loginError.value = task.exception?.message // Fehlermeldung wird gesetzt
                }
            }
    }
}