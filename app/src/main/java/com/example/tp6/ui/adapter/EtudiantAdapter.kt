package com.example.tp6.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tp6.data.entity.Etudiant
import com.example.tp6.databinding.ItemEtudiantBinding

class EtudiantAdapter(
    private val onLongClick: (Etudiant) -> Unit,
    private val onClick: (Etudiant) -> Unit
) : RecyclerView.Adapter<EtudiantAdapter.EtudiantViewHolder>() {

    private var listeEtudiants = emptyList<Etudiant>()

    class EtudiantViewHolder(
        val binding: ItemEtudiantBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EtudiantViewHolder {
        val binding = ItemEtudiantBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EtudiantViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: EtudiantViewHolder,
        position: Int
    ) {
        val etudiant = listeEtudiants[position]

        holder.binding.textMail.text = etudiant.mail
        holder.binding.textClasse.text = etudiant.classe

        // Clic simple : modification
        holder.itemView.setOnClickListener {
            onClick(etudiant)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(etudiant)
            true
        }
    }

    override fun getItemCount(): Int = listeEtudiants.size

    fun setEtudiants(etudiants: List<Etudiant>) {
        listeEtudiants = etudiants
        notifyDataSetChanged()
    }
}