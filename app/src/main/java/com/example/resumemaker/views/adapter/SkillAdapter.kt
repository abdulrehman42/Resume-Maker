package com.example.resumemaker.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.SkillitemsBinding
import com.example.resumemaker.databinding.SuggestionitemBinding
import com.example.resumemaker.models.SuggestionModel

class SkillAdapter(val context: Context, val list:List<SuggestionModel>,val onclick:(SuggestionModel)->Unit): RecyclerView.Adapter<SkillAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: SkillitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(model: SuggestionModel) {
            binding.skillname.text=model.skillName
            binding.editSkill.setOnClickListener{
                onclick(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SkillitemsBinding.inflate(
            LayoutInflater.from(context),
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