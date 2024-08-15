package com.example.resumemaker.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.EducationitemsBinding
import com.example.resumemaker.databinding.ProfileexperienceitemBinding
import com.example.resumemaker.models.EducationModel
import com.example.resumemaker.models.ExperienceModel

class ExperienceProfAdapter (val context: Context, val list:List<ExperienceModel>,val onclick:(ExperienceModel)->Unit): RecyclerView.Adapter<ExperienceProfAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ProfileexperienceitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(model: ExperienceModel) {
            binding.experiencename.text=model.fieldName
            binding.officeName.text=model.officeName+"-Full Time"
            binding.experienceTime.text=model.experienceTime
            binding.officeAddress.text=model.officeAddress
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProfileexperienceitemBinding.inflate(
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