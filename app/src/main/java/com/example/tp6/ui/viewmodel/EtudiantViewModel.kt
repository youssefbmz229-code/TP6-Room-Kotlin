
package com.example.tp6.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp6.data.RetrofitInstance
import com.example.tp6.data.entity.Etudiant
import com.example.tp6.data.repository.EtudiantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EtudiantViewModel(
    private val repository: EtudiantRepository
) : ViewModel() {

    // Liste des étudiants récupérée depuis l'API
    private val _etudiants = MutableLiveData<MutableList<Etudiant>>()
    val etudiants: LiveData<MutableList<Etudiant>> = _etudiants

    // Gestion des erreurs
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Ancienne liste Room conservée
    val allEtudiants: LiveData<List<Etudiant>> =
        repository.allEtudiants

    init {
        getAllEtudiants()
    }

    // Récupération depuis l'API
    private fun getAllEtudiants() {
        _errorMessage.postValue(null)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getEtudiants()

                if (response.isSuccessful && response.body() != null) {
                    val list = response.body()!!

                    if (list.isNotEmpty()) {
                        _etudiants.postValue(list.toMutableList())
                        _errorMessage.postValue(null)
                    } else {
                        _etudiants.postValue(mutableListOf())
                        _errorMessage.postValue("Aucun étudiant trouvé.")
                    }

                } else {
                    val errorMsg = "Erreur de serveur: ${response.code()}"
                    _errorMessage.postValue(errorMsg)
                    _etudiants.postValue(mutableListOf())
                    Log.e("API_CALL", errorMsg)
                }

            } catch (e: Exception) {
                val errorMsg = "Erreur de connexion. Vérifiez l'URL de l'API."
                _errorMessage.postValue(errorMsg)
                _etudiants.postValue(mutableListOf())
                Log.e("API_CALL", "Erreur réseau: ${e.message}", e)
            }
        }
    }

    // Fonctions Room conservées
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

    fun getEtudiantById(cin: Int, onResult: (Etudiant?) -> Unit) {
        viewModelScope.launch {
            onResult(repository.getEtudiantById(cin))
        }
    }

    fun searchEtudiantsByClasse(classe: String): LiveData<List<Etudiant>> {
        return repository.searchEtudiantsByClasse(classe)
    }
    fun addEtudiantApi(etudiant: Etudiant) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.addEtudiant(etudiant)

                if (response.isSuccessful) {
                    getAllEtudiants()
                    _errorMessage.postValue(null)
                } else {
                    _errorMessage.postValue(
                        "Erreur lors de l'ajout : ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }

    fun updateEtudiantApi(etudiant: Etudiant) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.updateEtudiant(etudiant)

                if (response.isSuccessful) {
                    getAllEtudiants()
                    _errorMessage.postValue(null)
                } else {
                    _errorMessage.postValue("Erreur modification : ${response.code()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }

    fun deleteEtudiantApi(cin: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.deleteEtudiant(cin)

                if (response.isSuccessful) {
                    getAllEtudiants()
                    _errorMessage.postValue(null)
                } else {
                    _errorMessage.postValue("Erreur suppression : ${response.code()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue(e.message)
            }
        }
    }
}
