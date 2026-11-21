
package com.example.tp6.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp6.data.entity.Etudiant
import com.example.tp6.data.repository.EtudiantRepository
import kotlinx.coroutines.launch

class EtudiantViewModel(
    private val repository: EtudiantRepository
) : ViewModel() {

    val allEtudiants: LiveData<List<Etudiant>> =
        repository.allEtudiants

    fun insert(etudiant: Etudiant) {
        viewModelScope.launch {
            repository.insert(etudiant)
        }
    }

    fun delete(etudiant: Etudiant) {
        viewModelScope.launch {
            repository.delete(etudiant)
        }
    }

    fun update(etudiant: Etudiant) {
        viewModelScope.launch {
            repository.update(etudiant)
        }
    }

    fun getEtudiantById(id: Int, onResult: (Etudiant?) -> Unit) {
        viewModelScope.launch {
            onResult(repository.getEtudiantById(id))
        }
    }
    fun searchEtudiantsByClasse(classe: String): LiveData<List<Etudiant>> {
        return repository.searchEtudiantsByClasse(classe)
    }
}
