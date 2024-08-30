package com.example.shadowwisper.ui.theme.data.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterViewModel : ViewModel() {

    private val _registrationSuccess = MutableLiveData<Boolean>()
    val registrationSuccess: LiveData<Boolean> = _registrationSuccess

    private val _registrationError = MutableLiveData<String>()
    val registrationError: LiveData<String> = _registrationError

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _registrationSuccess.value = true
                } else {
                    _registrationError.value = task.exception?.message
                }
            }
    }
}