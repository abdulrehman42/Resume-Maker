package com.pentabit.cvmaker.resumebuilder.views.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pentabit.cvmaker.resumebuilder.databinding.SuggestionitemBinding
import com.pentabit.cvmaker.resumebuilder.models.api.LookUpResponse

class SuggestionAdapter(val list: List<LookUpResponse>, val onclick:(LookUpResponse)->Unit): RecyclerView.Adapter<SuggestionAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: SuggestionitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(model: LookUpResponse) {
            binding.name.text=model.text
            binding.name.setOnClickListener {
                onclick(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SuggestionitemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
    }


}