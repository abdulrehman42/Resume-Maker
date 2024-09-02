package com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pentabit.cvmaker.resumebuilder.databinding.EducationitemsBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.utils.Helper

class ExperienceAdapter: ListAdapter<ProfileModelAddDetailResponse.UserExperience, ExperienceAdapter.ViewHolder>(TemplateDiffCallback) {
    inner class ViewHolder(private val binding: EducationitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun setData(model: ProfileModelAddDetailResponse.UserExperience) {
            binding.universityname.text= Helper.removeOneUnderscores(model.title)
            binding.degreeName.text=model.description
            binding.degreeYears.text=Helper.formatDateRange(model.startDate,model.endDate)
            binding.editEdu.setOnClickListener {
                editItemClickCallback?.invoke(model,position)
            }
            binding.deleteEdu.setOnClickListener {
                deleteitemClickCallback?.invoke(position)
            }
        }
    }

    var editItemClickCallback: ((ProfileModelAddDetailResponse.UserExperience,Int) -> Unit)? = null
    var deleteitemClickCallback: ((Int) -> Unit)? = null


    fun setOnEditItemClickCallback(callback: (ProfileModelAddDetailResponse.UserExperience,Int) -> Unit) {
        this.editItemClickCallback = callback
    }

    fun setOnItemDeleteClickCallback(callback: (Int) -> Unit) {
        this.deleteitemClickCallback = callback
    }

    object TemplateDiffCallback : DiffUtil.ItemCallback<ProfileModelAddDetailResponse.UserExperience>() {
        override fun areItemsTheSame(oldItem: ProfileModelAddDetailResponse.UserExperience, newItem: ProfileModelAddDetailResponse.UserExperience): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ProfileModelAddDetailResponse.UserExperience, newItem: ProfileModelAddDetailResponse.UserExperience): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EducationitemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(getItem(position))
    }
}
