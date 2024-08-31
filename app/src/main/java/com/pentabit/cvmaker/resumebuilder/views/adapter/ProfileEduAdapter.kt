package com.pentabit.cvmaker.resumebuilder.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pentabit.cvmaker.resumebuilder.databinding.ProfileeducBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.pentabitessentials.views.AppsKitSDKRecyclerBaseViewBinding

class ProfileEduAdapter(): ListAdapter<ProfileModelAddDetailResponse.UserQualification, AppsKitSDKRecyclerBaseViewBinding>(AddEducationDiffCallback){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppsKitSDKRecyclerBaseViewBinding {
        val binding = ProfileeducBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppsKitSDKRecyclerBaseViewBinding(binding)
    }


    override fun onBindViewHolder(holder: AppsKitSDKRecyclerBaseViewBinding, position: Int) {
        val binding = holder.binding as ProfileeducBinding
        val model = getItem(position)
        binding.universityname.text= Helper.removeOneUnderscores(model.institute)
        binding.degreeName.text= Helper.removeOneUnderscores(model.degree)
        binding.degreeYears.text= Helper.formatDateRangeYearOnly(model.startDate,model.endDate)

    }
    object AddEducationDiffCallback : DiffUtil.ItemCallback<ProfileModelAddDetailResponse.UserQualification>() {
        override fun areItemsTheSame(
            oldItem: ProfileModelAddDetailResponse.UserQualification,
            newItem: ProfileModelAddDetailResponse.UserQualification
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: ProfileModelAddDetailResponse.UserQualification,
            newItem: ProfileModelAddDetailResponse.UserQualification
        ): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }


}