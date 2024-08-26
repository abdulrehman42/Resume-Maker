package com.pentabit.cvmaker.resumebuilder.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pentabit.cvmaker.resumebuilder.databinding.ProfileskillitemsBinding
import com.pentabit.cvmaker.resumebuilder.utils.Helper

class SkillProfAdapter(val context: Context, val list: List<String>): RecyclerView.Adapter<SkillProfAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ProfileskillitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(model: String) {
            binding.skillName.text=Helper.removeOneUnderscores(model)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProfileskillitemsBinding.inflate(
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