package com.example.notesapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notesapp.api.NotesAPI
import com.example.notesapp.models.NoteRequest
import com.example.notesapp.models.NoteResponse
import com.example.notesapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val notesAPI: NotesAPI) {

    private val _noteResponseLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesLiveData: LiveData<NetworkResult<List<NoteResponse>>>
    get() = _noteResponseLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>>
    get() = _statusLiveData

    /**
     * get all notes method
     */
    suspend fun getAllNotes(){
        _noteResponseLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.getAllNotes()
        if(response.isSuccessful && response.body()!=null){
            _noteResponseLiveData.postValue(NetworkResult.Success(response.body()))
        }else if(response.errorBody()!= null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _noteResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))

        }else{
            _noteResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    /**
     * Create note method and gives response as a status
     */
    suspend fun createNote(noteRequest: NoteRequest){
        _noteResponseLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.createNote(noteRequest)
        handleResponseStatus(response, "Note Created")
    }

    /**
     * update note
     */

    suspend fun updateNote(id: String, noteRequest: NoteRequest){
        _noteResponseLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.updateNote(id, noteRequest)
        handleResponseStatus(response, "Note Updated")
    }

    suspend fun deleteNote(id:String){
        _noteResponseLiveData.postValue(NetworkResult.Loading())
        val response = notesAPI.deleteNote(id)
        handleResponseStatus(response, "Note Deleted")
    }

    //handle response status
    private fun handleResponseStatus(response: Response<NoteResponse>, status: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(status))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))

        }
    }
}