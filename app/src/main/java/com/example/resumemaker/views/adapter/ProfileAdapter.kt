package com.example.resumemaker.views.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.resumemaker.databinding.CustomprofileitemBinding
import com.example.resumemaker.models.ProfileModel
import com.example.resumemaker.models.api.ProfileListingModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes.alertboxChooseProfile

class ProfileAdapter(
    val context: Activity, val list: List<ProfileListingModel>,
    val onclick:(ProfileListingModel)->Unit): RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: CustomprofileitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: ProfileListingModel) {
            binding.name.text = model.name
            binding.profession.text=model.jobTitle
            Glide.with(binding.imageProfile.context).load(Constants.BASE_MEDIA_URL+model.path).into(binding.imageProfile)
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