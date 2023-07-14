package com.example.notesapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.models.NoteRequest
import com.example.notesapp.repositories.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository): ViewModel() {

    val notesLiveData get() =  noteRepository.notesLiveData
    val statusLiveData get()=  noteRepository.statusLiveData

    //create note method
    fun createNote(noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.createNote(noteRequest)
        }
    }

    //get all notes
    fun getAllNotes(){
//        viewModelScope.launch {
//            noteRepository.getAllNotes()
//        }
        CoroutineScope(Dispatchers.IO).launch {
            noteRepository.getAllNotes()
        }
    }

    //update note
    fun updateNote(id: String, noteRequest: NoteRequest){
        viewModelScope.launch {
            noteRepository.updateNote(id, noteRequest)
        }
    }

    //delete note
    fun deleteNote(id: String){
        viewModelScope.launch {
            noteRepository.deleteNote(id)
        }
    }
}