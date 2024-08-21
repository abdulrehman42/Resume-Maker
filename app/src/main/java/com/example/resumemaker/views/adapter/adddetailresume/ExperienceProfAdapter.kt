package com.example.resumemaker.views.adapter.adddetailresume

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.ProfileexperienceitemBinding
import com.example.resumemaker.models.ExperienceModel
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse

class ExperienceProfAdapter(
    val context: Context, val list: List<ProfileModelAddDetailResponse.UserExperience>): RecyclerView.Adapter<ExperienceProfAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ProfileexperienceitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(model: ProfileModelAddDetailResponse.UserExperience) {
            binding.experiencename.text=model.title
            binding.officeName.text=model.company+"-Full Time"
            binding.experienceTime.text=model.employmentType
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