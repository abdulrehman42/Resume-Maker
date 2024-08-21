package com.example.resumemaker.views.adapter.adddetailresume

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.EducationitemsBinding
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse

class EducationAdapter(
    val context: Context, val list: List<ProfileModelAddDetailResponse.UserQualification>,
    val check:Boolean,
    val onclick:(ProfileModelAddDetailResponse.UserQualification)->Unit): RecyclerView.Adapter<EducationAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: EducationitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(model: ProfileModelAddDetailResponse.UserQualification) {
            binding.universityname.text=model.institute
            binding.degreeName.text=model.degree
            binding.degreeYears.text=model.startDate+"-"+model.endDate
            binding.editEdu.setOnClickListener {
                onclick(model)
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