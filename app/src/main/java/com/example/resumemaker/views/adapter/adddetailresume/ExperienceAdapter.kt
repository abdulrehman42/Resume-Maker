package com.example.resumemaker.views.adapter.adddetailresume

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.EducationitemsBinding
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse

class ExperienceAdapter: ListAdapter<ProfileModelAddDetailResponse.UserExperience, ExperienceAdapter.ViewHolder>(TemplateDiffCallback) {
    inner class ViewHolder(private val binding: EducationitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: ProfileModelAddDetailResponse.UserExperience) {
            binding.universityname.text=model.title
            binding.degreeName.text=model.description
            binding.degreeYears.text=model.startDate+"-"+model.endDate
            binding.editEdu.setOnClickListener {
                editItemClickCallback?.invoke(model)
            }
            binding.deleteEdu.setOnClickListener {
                deleteitemClickCallback?.invoke(position)
            }
        }
    }

    var editItemClickCallback: ((ProfileModelAddDetailResponse.UserExperience) -> Unit)? = null
    var deleteitemClickCallback: ((Int) -> Unit)? = null


    fun setOnEditItemClickCallback(callback: (ProfileModelAddDetailResponse.UserExperience) -> Unit) {
        this.editItemClickCallback = callback
    }

    fun setOnItemDeleteClickCallback(callback: (Int) -> Unit) {
        this.deleteitemClickCallback = callback
    }

    object TemplateDiffCallback : DiffUtil.ItemCallback<ProfileModelAddDetailResponse.UserExperience>() {
        override fun areItemsTheSame(oldItem: ProfileModelAddDetailResponse.UserExperience, newItem: ProfileModelAddDetailResponse.UserExperience): Boolean {
            return oldItem.id == newItem.id
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
