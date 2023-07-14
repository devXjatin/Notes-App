package com.example.notesapp.models

import com.google.gson.annotations.SerializedName

data class NoteResponse(
    @SerializedName("_id")
    val id: String = "",
    val description: String = "",
    val title: String = "",
    val userId: String = "",
)