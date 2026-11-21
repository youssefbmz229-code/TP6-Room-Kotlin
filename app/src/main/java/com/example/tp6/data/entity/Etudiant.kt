
package com.example.tp6.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "etudiant_table")
data class Etudiant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val mail: String,

    val classe: String
)
