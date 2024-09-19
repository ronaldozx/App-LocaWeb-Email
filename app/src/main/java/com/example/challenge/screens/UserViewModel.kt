package com.example.challenge.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

data class UserData(
    val nome: String = "",
    val email: String = "",
    val telefone: String = "",
    val dtNascimento: String = ""
)

fun getCurrentUserId(): String? {
    return FirebaseAuth.getInstance().currentUser?.uid
}

suspend fun fetchUserData(userId: String): UserData {
    val db = FirebaseFirestore.getInstance()
    val document = db.collection("users").document(userId).get().await()
    return document.toObject(UserData::class.java) ?: UserData()
}



class UserViewModel : ViewModel() {
    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> = _userData

    fun fetchUserData() {
        viewModelScope.launch {
            val userId = getCurrentUserId()
            if (userId != null) {
                val userData = fetchUserData(userId)
                _userData.value = userData
            }
        }
    }
}
