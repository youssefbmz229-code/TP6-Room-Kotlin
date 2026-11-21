
package com.example.tp6.ui.fragments

import android.R.attr.onClick
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tp6.app.EtudiantApplication
import com.example.tp6.databinding.FragmentListeEtudiantsBinding
import com.example.tp6.ui.adapter.EtudiantAdapter
import com.example.tp6.ui.viewmodel.EtudiantViewModel
import com.example.tp6.ui.viewmodel.EtudiantViewModelFactory
import androidx.navigation.fragment.findNavController
//import androidx.navigation.fragment.R
import com.example.tp6.R
import androidx.appcompat.app.AlertDialog
import android.widget.Toast
import android.widget.EditText
import android.widget.SearchView
import com.example.tp6.data.entity.Etudiant
class ListeEtudiantsFragment : Fragment() {

    private var _binding: FragmentListeEtudiantsBinding? = null
    private val binding get() = _binding!!

    private var etudiantsApi: List<Etudiant> = emptyList()

    private lateinit var adapter: EtudiantAdapter

    private val viewModel: EtudiantViewModel by activityViewModels {
        val repository =
            (requireActivity().application as EtudiantApplication).repository

        EtudiantViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListeEtudiantsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EtudiantAdapter(
            onLongClick = { etudiant ->

                AlertDialog.Builder(requireContext())
                    .setTitle("Supprimer étudiant")
                    .setMessage("Voulez-vous vraiment supprimer ${etudiant.mail} ?")
                    .setPositiveButton("Oui") { _, _ ->
                        viewModel.deleteEtudiantApi(etudiant.cin)

                        Toast.makeText(
                            requireContext(),
                            "Étudiant supprimé",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("Non", null)
                    .show()
            },

            onClick = { etudiant ->

                val dialogView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_modifier_etudiant, null)

                val editMail = dialogView.findViewById<EditText>(R.id.editMail)
                val editClasse = dialogView.findViewById<EditText>(R.id.editClasse)

                editMail.setText(etudiant.mail)
                editClasse.setText(etudiant.classe)

                AlertDialog.Builder(requireContext())
                    .setTitle("Modifier étudiant")
                    .setView(dialogView)
                    .setPositiveButton("Enregistrer") { _, _ ->

                        val nouvelEtudiant = etudiant.copy(
                            mail = editMail.text.toString().trim(),
                            classe = editClasse.text.toString().trim()
                        )

                        viewModel.updateEtudiantApi(nouvelEtudiant)

                        Toast.makeText(
                            requireContext(),
                            "Étudiant modifié",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .setNegativeButton("Annuler", null)
                    .show()

            }
        )

        binding.recyclerViewEtudiants.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerViewEtudiants.adapter = adapter

        viewModel.etudiants.observe(viewLifecycleOwner) { etudiants ->
            etudiantsApi = etudiants

            if (etudiants.isEmpty()) {
                binding.recyclerViewEtudiants.visibility = View.GONE
                binding.textError.visibility = View.VISIBLE
                binding.textError.text = "Aucun étudiant trouvé."
            } else {
                binding.recyclerViewEtudiants.visibility = View.VISIBLE
                binding.textError.visibility = View.GONE
                adapter.setEtudiants(etudiants)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (!message.isNullOrEmpty()) {
                binding.textError.text = message
                binding.textError.visibility = View.VISIBLE
                binding.recyclerViewEtudiants.visibility = View.GONE
            }
        }
        binding.buttonVersAjout.setOnClickListener {
            findNavController().navigate(R.id.action_liste_to_ajout)
        }

        binding.searchViewClasse.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    rechercherEtudiants(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    rechercherEtudiants(newText)
                    return true
                }
            }
        )
    }
    private fun rechercherEtudiants(classe: String?) {
        val resultat = if (classe.isNullOrBlank()) {
            etudiantsApi
        } else {
            etudiantsApi.filter {
                it.classe.contains(classe, ignoreCase = true)
            }
        }

        adapter.setEtudiants(resultat)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
