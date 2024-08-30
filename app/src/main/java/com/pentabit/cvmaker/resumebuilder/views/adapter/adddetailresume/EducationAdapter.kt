package com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pentabit.cvmaker.resumebuilder.databinding.EducationitemsBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.pentabitessentials.views.AppsKitSDKRecyclerBaseViewBinding

class EducationAdapter(
    val check:Boolean,
    val onclick:(ProfileModelAddDetailResponse.UserQualification,Int)->Unit,val onDelete:(Int)->Unit): ListAdapter<ProfileModelAddDetailResponse.UserQualification, AppsKitSDKRecyclerBaseViewBinding>(AddEducationDiffCallback){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppsKitSDKRecyclerBaseViewBinding {
        val binding = EducationitemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppsKitSDKRecyclerBaseViewBinding(binding)
    }


    override fun onBindViewHolder(holder: AppsKitSDKRecyclerBaseViewBinding, position: Int) {
        val binding = holder.binding as EducationitemsBinding
        val model = getItem(position)
        binding.universityname.text= Helper.removeOneUnderscores(model.institute)
        binding.degreeName.text= Helper.removeOneUnderscores(model.degree)
        binding.degreeYears.text= Helper.formatDateRangeYearOnly(model.startDate,model.endDate)
        binding.editEdu.setOnClickListener {
            onclick(model,position)
        }
        binding.deleteEdu.setOnClickListener {
            onDelete(position)
        }
        if (check)
        {
            binding.editEdu.isGone=true
            binding.deleteEdu.isGone=true
        }
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