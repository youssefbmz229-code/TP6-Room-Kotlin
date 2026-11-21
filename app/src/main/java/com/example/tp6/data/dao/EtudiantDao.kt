
package com.example.tp6.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.tp6.data.entity.Etudiant

@Dao
interface EtudiantDao {

    // Afficher tous les étudiants
    @Query("SELECT * FROM etudiant_table")
    fun getAllEtudiants(): LiveData<List<Etudiant>>

    // Ajouter un étudiant
    @Insert
    suspend fun insert(etudiant: Etudiant)

    // Supprimer un étudiant
    @Delete
    suspend fun delete(etudiant: Etudiant)

    // Modifier un étudiant
    @Update
    suspend fun update(etudiant: Etudiant)

    // Rechercher un étudiant par son ID
    @Query("SELECT * FROM etudiant_table WHERE id = :id")
    suspend fun getEtudiantById(id: Int): Etudiant?

    @Query("SELECT * FROM etudiant_table WHERE classe LIKE '%' || :classe || '%'")
    fun searchEtudiantsByClasse(classe: String): LiveData<List<Etudiant>>
}
