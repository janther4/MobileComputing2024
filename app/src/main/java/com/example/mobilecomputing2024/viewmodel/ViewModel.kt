package com.example.mobilecomputing2024.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecomputing2024.MainActivity
import com.example.mobilecomputing2024.entity.ImgEntity
import com.example.mobilecomputing2024.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    // Inject the UserDao
    private val userDao = MainActivity.database.userDao()
    private val imgDao = MainActivity.database.imgDao()

    // MutableState for the text entered in the OutlinedTextField
    var text by mutableStateOf("")

    private val _latestUser = MutableStateFlow<UserEntity?>(null)
    val latestUser: StateFlow<UserEntity?> = _latestUser

    private val _latestImg = MutableStateFlow<ImgEntity?>(null)
    val latestImg: StateFlow<ImgEntity?> = _latestImg


    init {
        // Load the list of users when ViewModel is initialized
        loadLatestUser()
        loadLatestImg()
    }

    private fun loadLatestImg() {
        viewModelScope.launch {
            // Fetch the list of users from the database
            val latestImg = imgDao.getLatestImg()
            // Update the StateFlow with the fetched data
            _latestImg.value = latestImg
            // debug
            Log.d("saatanansaatana", _latestImg.value?.imgUri ?:"jeejee")
        }
    }

    private fun loadLatestUser() {
        viewModelScope.launch {
            // Fetch the list of users from the database
            val latestUser = userDao.getLatestUser()
            // Update the StateFlow with the fetched data
            _latestUser.value = latestUser
        }
    }

    // Function to handle the button click or any other relevant action
    fun onSaveButtonClick() {
        viewModelScope.launch(Dispatchers.IO) {
        val enteredText = text.trim()

        if (enteredText.isNotEmpty()) {
            // Create a User object and insert it into the database
            val user = UserEntity(name = enteredText)
            // Assuming you have a suspend function in your UserDao for inserting a user
            userDao.insertUser(user)

            viewModelScope.launch(Dispatchers.IO) {
                loadLatestUser()
            }

            // Optionally, clear the text field after saving
            //text = ""
        }
        }
    }

    fun saveImageToDatabase(imgUri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val img = ImgEntity(imgUri = imgUri)
            imgDao.insertImg(img)
            loadLatestImg()
        }
    }
}

