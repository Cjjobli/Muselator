package com.example.muselator.Firebase.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

/**
 * ViewModel that manages user authentication using Firebase Authentication.
 * It exposes an observable authentication state and provides methods for
 * login, signup, signout, and checking authentication status.
 */
class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    /**
     * Checks if a user is currently authenticated and updates the auth state.
     */
    fun checkAuthStatus(){
        if(auth.currentUser==null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }

    /**
     * Attempts to sign in the user with the given email and password.
     * Updates the auth state based on the result of the login attempt.
     *
     * @param email User's email address.
     * @param password User's password.
     */
    fun login(email: String, password: String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task ->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    /**
     * Attempts to create a new user account with the given email and password.
     * Updates the auth state based on the result of the signup attempt.
     *
     * @param email New user's email address.
     * @param password New user's password.
     */
    fun signup(email: String, password: String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{task ->
                if (task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value =
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }

    /**
     * Signs out the current user and updates the auth state to unauthenticated.
     */
    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

/**
 * Represents the different states of user authentication.
 */
sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()

    data class Error(val message : String) : AuthState()
}