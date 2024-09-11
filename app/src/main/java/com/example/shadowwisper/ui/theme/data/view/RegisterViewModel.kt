/**
 * ViewModel-Klasse `RegisterViewModel`, die für die Verwaltung des Registrierungsprozesses zuständig ist.
 * Diese Klasse verwendet Firebase Authentication, um Benutzer mit E-Mail und Passwort zu registrieren.
 * Sie stellt LiveData-Objekte zur Verfügung, um den Erfolg oder Fehler des Registrierungsprozesses zu beobachten.
 */
package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * ViewModel zur Verwaltung des Registrierungsprozesses.
 * Diese Klasse greift auf Firebase Authentication zu, um neue Benutzer zu registrieren.
 * Es bietet zwei LiveData-Objekte: Eines für den Registrierungs-Erfolg und eines für mögliche Fehlermeldungen.
 */
class RegisterViewModel : ViewModel() {

    // Privates MutableLiveData, das den Erfolg der Registrierung speichert.
    private val _registrationSuccess = MutableLiveData<Boolean>()

    /**
     * Öffentliches LiveData-Objekt, das von der UI beobachtet werden kann.
     * Es gibt `true` zurück, wenn die Registrierung erfolgreich war.
     */
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    // Privates MutableLiveData, das Fehlermeldungen während des Registrierungsprozesses speichert.
    private val _registrationError = MutableLiveData<String>()

    /**
     * Öffentliches LiveData-Objekt, das von der UI beobachtet werden kann.
     * Es enthält eine Fehlermeldung, falls die Registrierung fehlgeschlagen ist.
     */
    val registrationError: LiveData<String> = _registrationError

    // Firebase Authentication-Instanz zur Erstellung eines neuen Benutzers.
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /**
     * Führt die Registrierung eines neuen Benutzers mit E-Mail und Passwort durch.
     * Wenn die Registrierung erfolgreich ist, wird `registrationSuccess` auf `true` gesetzt.
     * Wenn die Registrierung fehlschlägt, wird `registrationError` mit der Fehlermeldung befüllt.
     * @param email Die E-Mail-Adresse des neuen Benutzers.
     * @param password Das Passwort des neuen Benutzers.
     */
    fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _registrationSuccess.value = true // Registrierung erfolgreich
                } else {
                    _registrationError.value = task.exception?.message // Fehlermeldung wird gesetzt
                }
            }
    }
}