
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

class ListeEtudiantsFragment : Fragment() {

    private var _binding: FragmentListeEtudiantsBinding? = null
    private val binding get() = _binding!!

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
                        viewModel.delete(etudiant)

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

                        viewModel.update(nouvelEtudiant)

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

        viewModel.allEtudiants.observe(viewLifecycleOwner) { etudiants ->
            adapter.setEtudiants(etudiants)
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
        if (classe.isNullOrBlank()) {
            viewModel.allEtudiants.observe(viewLifecycleOwner) { etudiants ->
                adapter.setEtudiants(etudiants)
            }
        } else {
            viewModel.searchEtudiantsByClasse(classe)
                .observe(viewLifecycleOwner) { etudiants ->
                    adapter.setEtudiants(etudiants)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
