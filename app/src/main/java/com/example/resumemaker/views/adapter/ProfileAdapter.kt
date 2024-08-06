package com.example.resumemaker.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.CustomprofileitemBinding
import com.example.resumemaker.models.ProfileModel

class ProfileAdapter(val context: Context,val list: List<ProfileModel>): RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: CustomprofileitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: ProfileModel) {
            binding.name.text = model.name
            binding.profession.text=model.profession

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomprofileitemBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
    }



}