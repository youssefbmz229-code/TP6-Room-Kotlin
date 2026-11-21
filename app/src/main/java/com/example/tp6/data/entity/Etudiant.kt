package com.example.tp6.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "etudiant_table")
data class Etudiant(

    @PrimaryKey
    val cin: Int,

    val nom: String,
    val prenom: String,
    val mail: String,
    val password: String,
    val classe: String
)