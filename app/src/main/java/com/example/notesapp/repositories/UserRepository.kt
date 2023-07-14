package com.example.notesapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesapp.api.UserAPI
import com.example.notesapp.models.UserRequest
import com.example.notesapp.models.UserResponse
import com.example.notesapp.utils.Constants.TAG
import com.example.notesapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    private var _userResponseLiveData= MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
    get() = _userResponseLiveData

    suspend fun userRegister(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signup(userRequest)
//        Log.d(TAG, response.body().toString())
        handleResponse(response)

    }

    suspend fun userLogin(userRequest: UserRequest){
        val response = userAPI.signin(userRequest)
//        Log.d(TAG, response.body().toString())
        handleResponse(response)

    }

    private fun handleResponse(response: Response<UserResponse>){
        if(response.isSuccessful && response.body()!=null){
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()))
        }else if(response.errorBody()!= null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))

        }else{
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}