package com.example.mobilecomputing2024.viewmodel

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

    private val userDao = MainActivity.database.userDao()
    private val imgDao = MainActivity.database.imgDao()

    var text by mutableStateOf("")

    private val _latestUser = MutableStateFlow<UserEntity?>(null)
    val latestUser: StateFlow<UserEntity?> = _latestUser

    private val _latestImg = MutableStateFlow<ImgEntity?>(null)
    val latestImg: StateFlow<ImgEntity?> = _latestImg


    init {
        loadLatestUser()
        loadLatestImg()
    }

    private fun loadLatestImg() {
        viewModelScope.launch {
            val latestImg = imgDao.getLatestImg()
            _latestImg.value = latestImg
        }
    }

    private fun loadLatestUser() {
        viewModelScope.launch {
            val latestUser = userDao.getLatestUser()
            _latestUser.value = latestUser
        }
    }

    fun onSaveButtonClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val enteredText = text.trim()

            if (enteredText.isNotEmpty()) {
                val user = UserEntity(name = enteredText)
                userDao.insertUser(user)

                viewModelScope.launch(Dispatchers.IO) {
                    loadLatestUser()
                }
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
