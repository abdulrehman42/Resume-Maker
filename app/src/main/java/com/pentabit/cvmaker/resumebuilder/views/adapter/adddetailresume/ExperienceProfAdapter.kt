package com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.pentabit.cvmaker.resumebuilder.databinding.ProfileexperienceitemBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.utils.Helper

class ExperienceProfAdapter(
    val context: Context, val list: List<ProfileModelAddDetailResponse.UserExperience>): RecyclerView.Adapter<ExperienceProfAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ProfileexperienceitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun setData(model: ProfileModelAddDetailResponse.UserExperience) {
            binding.experiencename.text=model.title
            binding.officeName.text=model.company+"- "+model.employmentType
            binding.experienceTime.text= com.pentabit.cvmaker.resumebuilder.utils.Helper.formatDateRange(model.startDate,model.endDate)
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
    }


}