package com.example.resumemaker.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.EducationitemsBinding
import com.example.resumemaker.databinding.SampleitemsBinding
import com.example.resumemaker.models.EducationModel
import com.example.resumemaker.models.SampleModel

class EducationAdapter(val context: Context, val list:List<EducationModel>,val check:Boolean,val onclick:(EducationModel)->Unit): RecyclerView.Adapter<EducationAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: EducationitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun setData(model: EducationModel) {
            binding.universityname.text=model.universityName
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