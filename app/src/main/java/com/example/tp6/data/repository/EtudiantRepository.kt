
package com.example.tp6.data.repository

import androidx.lifecycle.LiveData
import com.example.tp6.data.dao.EtudiantDao
import com.example.tp6.data.entity.Etudiant

class EtudiantRepository(
    private val etudiantDao: EtudiantDao
) {

    val allEtudiants: LiveData<List<Etudiant>> =
        etudiantDao.getAllEtudiants()

    suspend fun insert(etudiant: Etudiant) {
        etudiantDao.insert(etudiant)
    }

    suspend fun delete(etudiant: Etudiant) {
        etudiantDao.delete(etudiant)
    }

    suspend fun update(etudiant: Etudiant) {
        etudiantDao.update(etudiant)
    }

    suspend fun getEtudiantById(id: Int): Etudiant? {
        return etudiantDao.getEtudiantById(id)
    }

    fun searchEtudiantsByClasse(classe: String): LiveData<List<Etudiant>> {
        return etudiantDao.searchEtudiantsByClasse(classe)
    }
}
