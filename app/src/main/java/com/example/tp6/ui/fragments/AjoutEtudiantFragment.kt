
package com.example.tp6.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.tp6.app.EtudiantApplication
import com.example.tp6.data.entity.Etudiant
import com.example.tp6.databinding.FragmentAjoutEtudiantBinding
import com.example.tp6.ui.viewmodel.EtudiantViewModel
import com.example.tp6.ui.viewmodel.EtudiantViewModelFactory
import androidx.navigation.fragment.findNavController
import com.example.tp6.R

class AjoutEtudiantFragment : Fragment() {

    private var _binding: FragmentAjoutEtudiantBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentAjoutEtudiantBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAjouter.setOnClickListener {
            val mail = binding.editTextMail.text.toString().trim()
            val classe = binding.editTextClasse.text.toString().trim()


            if (mail.isEmpty() || classe.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Veuillez remplir tous les champs",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val etudiant = Etudiant(
                    mail = mail,
                    classe = classe
                )

                viewModel.insert(etudiant)
                findNavController().navigate(
                    R.id.action_nav_ajout_etudiant_to_nav_liste_etudiants
                )

                binding.editTextMail.text.clear()
                binding.editTextClasse.text.clear()

                Toast.makeText(
                    requireContext(),
                    "Étudiant ajouté avec succès",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
