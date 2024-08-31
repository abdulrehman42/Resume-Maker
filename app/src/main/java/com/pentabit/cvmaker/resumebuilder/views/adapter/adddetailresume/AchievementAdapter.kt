package com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pentabit.cvmaker.resumebuilder.databinding.EducationitemsBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.utils.Helper

class AchievementAdapter : ListAdapter<ProfileModelAddDetailResponse.UserAchievement, AchievementAdapter.ViewHolder>(TemplateDiffCallback) {
    inner class ViewHolder(private val binding: EducationitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: ProfileModelAddDetailResponse.UserAchievement) {
            binding.universityname.text=Helper.removeOneUnderscores(model.title)
            binding.degreeName.text=Helper.removeOneUnderscores(model.description)
            binding.degreeYears.text=Helper.convertIsoToCustomFormat(model.issueDate)
            binding.editEdu.setOnClickListener {
                editItemClickCallback?.invoke(model,position)
            }
            binding.deleteEdu.setOnClickListener {
                deleteitemClickCallback?.invoke(position)
            }
        }
    }

    var editItemClickCallback: ((ProfileModelAddDetailResponse.UserAchievement,Int) -> Unit)? = null
    var deleteitemClickCallback: ((Int) -> Unit)? = null


    fun setOnEditItemClickCallback(callback: (ProfileModelAddDetailResponse.UserAchievement,Int) -> Unit) {
        this.editItemClickCallback = callback
    }

    fun setOnItemDeleteClickCallback(callback: (Int) -> Unit) {
        this.deleteitemClickCallback = callback
    }

    object TemplateDiffCallback : DiffUtil.ItemCallback<ProfileModelAddDetailResponse.UserAchievement>() {
        override fun areItemsTheSame(oldItem: ProfileModelAddDetailResponse.UserAchievement, newItem: ProfileModelAddDetailResponse.UserAchievement): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProfileModelAddDetailResponse.UserAchievement, newItem: ProfileModelAddDetailResponse.UserAchievement): Boolean {
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
