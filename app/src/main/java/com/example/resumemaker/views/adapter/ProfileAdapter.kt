package com.example.resumemaker.views.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.CustomprofileitemBinding
import com.example.resumemaker.models.ProfileModelData
import com.example.resumemaker.utils.DialogueBoxes.alertboxChooseProfile

class ProfileAdapter(val context: Activity, val list: List<ProfileModelData>,val onclick:(ProfileModelData)->Unit): RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: CustomprofileitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: ProfileModelData) {
            binding.name.text = model.userName
            binding.profession.text=model.profession
            binding.textView4.setOnClickListener {
                onclick(model)
            }
            binding.settingMenu.setOnClickListener {
                alertboxChooseProfile(context)
            }

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