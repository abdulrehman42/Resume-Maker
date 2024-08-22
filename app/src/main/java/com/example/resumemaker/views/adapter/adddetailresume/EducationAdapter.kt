package com.example.resumemaker.views.adapter.adddetailresume

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.EducationitemsBinding
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.utils.Helper

class EducationAdapter(
    val context: Context, val list: List<ProfileModelAddDetailResponse.UserQualification>,
    val check:Boolean,
    val onclick:(ProfileModelAddDetailResponse.UserQualification)->Unit,val onDelete:(Int)->Unit): RecyclerView.Adapter<EducationAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: EducationitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(model: ProfileModelAddDetailResponse.UserQualification) {
            binding.universityname.text=model.institute
            binding.degreeName.text=model.degree
            binding.degreeYears.text=Helper.formatDateRangeYearOnly(model.startDate,model.endDate)
            binding.editEdu.setOnClickListener {
                onclick(model)
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EducationitemsBinding.inflate(
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