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

class ProjectAdapter : ListAdapter<ProfileModelAddDetailResponse.UserProject, ProjectAdapter.ViewHolder>(TemplateDiffCallback) {
    inner class ViewHolder(private val binding: EducationitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: ProfileModelAddDetailResponse.UserProject) {
            binding.degreeName.text=model.description
            binding.universityname.text=Helper.removeOneUnderscores(model.title)
            binding.editEdu.setOnClickListener {
                editItemClickCallback?.invoke(model,position)
            }
            binding.deleteEdu.setOnClickListener {
                deleteitemClickCallback?.invoke(position)
            }
        }
    }

    var editItemClickCallback: ((ProfileModelAddDetailResponse.UserProject,Int) -> Unit)? = null
    var deleteitemClickCallback: ((Int) -> Unit)? = null


    fun setOnEditItemClickCallback(callback: (ProfileModelAddDetailResponse.UserProject,Int) -> Unit) {
        this.editItemClickCallback = callback
    }

    fun setOnItemDeleteClickCallback(callback: (Int) -> Unit) {
        this.deleteitemClickCallback = callback
    }

    object TemplateDiffCallback : DiffUtil.ItemCallback<ProfileModelAddDetailResponse.UserProject>() {
        override fun areItemsTheSame(oldItem: ProfileModelAddDetailResponse.UserProject, newItem: ProfileModelAddDetailResponse.UserProject): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: ProfileModelAddDetailResponse.UserProject, newItem: ProfileModelAddDetailResponse.UserProject): Boolean {
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