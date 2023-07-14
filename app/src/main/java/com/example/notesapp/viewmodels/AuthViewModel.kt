package com.example.notesapp.viewmodels

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.models.UserRequest
import com.example.notesapp.models.UserResponse
import com.example.notesapp.repositories.UserRepository
import com.example.notesapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: UserRepository): ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
    get() = repository.userResponseLiveData

    fun userRegister(userRequest: UserRequest){
        viewModelScope.launch {
            repository.userRegister(userRequest)
        }
    }

    fun userLogin(userRequest: UserRequest){
        viewModelScope.launch {
            repository.userLogin(userRequest)
        }
    }

    //validate user input method
    fun validateInput(email: String, name:String, password: String, isLogin: Boolean):Pair<Boolean, String>{
        var result = Pair(true, "")
        if(!isLogin && TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please Enter Credentials")
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            result = Pair(false, "Enter valid Email")
        }else if(password.length<6){
            result = Pair(false, "Enter 6 digit password")
        }
        return result

    }
}