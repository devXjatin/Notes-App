package com.example.notesapp.api

import androidx.room.Delete
import com.example.notesapp.models.NoteRequest
import com.example.notesapp.models.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NotesAPI {

    @GET("note/notes")
    suspend fun getAllNotes(): Response<List<NoteResponse>>

    @POST("note/createnote")
    suspend fun createNote(@Body noteRequest: NoteRequest): Response<NoteResponse>

    @PUT("note/updatenote/{id}")
    suspend fun updateNote(@Path("id") id:String, @Body noteRequest: NoteRequest): Response<NoteResponse>

    @DELETE("note/deletenote/{id}")
    suspend fun deleteNote(@Path("id") id: String): Response<NoteResponse>
}