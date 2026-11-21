
package com.example.tp6.app

import android.app.Application
import com.example.tp6.data.database.EtudiantDatabase
import com.example.tp6.data.repository.EtudiantRepository

class EtudiantApplication : Application() {

    val database by lazy {
        EtudiantDatabase.getDatabase(this)
    }

    val repository by lazy {
        EtudiantRepository(database.etudiantDao())
    }
}
