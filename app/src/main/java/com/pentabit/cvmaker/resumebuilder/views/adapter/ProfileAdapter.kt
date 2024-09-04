package com.pentabit.cvmaker.resumebuilder.views.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.callbacks.ProfileItemCallbacks
import com.pentabit.cvmaker.resumebuilder.databinding.CustomprofileitemBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileListingModel
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.pentabitessentials.views.AppsKitSDKRecyclerBaseViewBinding
import java.io.File

class ProfileAdapter : ListAdapter<ProfileListingModel, AppsKitSDKRecyclerBaseViewBinding>(
    ProfileListingModelDiffCallback
) {
     var isViewProfile=false
     private var callback: ProfileItemCallbacks? = null

    fun setCallback(callback1: ProfileItemCallbacks) {
        callback = callback1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppsKitSDKRecyclerBaseViewBinding {
        val binding = CustomprofileitemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppsKitSDKRecyclerBaseViewBinding(binding)
    }

    override fun onBindViewHolder(holder: AppsKitSDKRecyclerBaseViewBinding, position: Int) {
        val binding = holder.binding as CustomprofileitemBinding
        val model = getItem(position)
        if (isViewProfile)
        {
            binding.textView4.setText("View Profile")
            binding.settingMenu.isGone=false
        }else{
            binding.settingMenu.isGone=true
        }
        binding.name.text = model.name.replaceFirstChar { it.uppercase() }
        binding.profession.text = Helper.removeOneUnderscores(model.jobTitle)
        Glide.with(binding.imageProfile.context)
            .load(com.pentabit.cvmaker.resumebuilder.utils.Constants.BASE_MEDIA_URL + model.path)
            .into(binding.imageProfile)
        binding.textView4.setOnClickListener {
            callback?.onItemClicked(model.id.toString())
        }
        binding.settingMenu.setOnClickListener {
            callback?.onOptionsClicked(model.id.toString())
        }
    }


    object ProfileListingModelDiffCallback : DiffUtil.ItemCallback<ProfileListingModel>() {
        override fun areItemsTheSame(
            oldItem: ProfileListingModel,
            newItem: ProfileListingModel
        ): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(
            oldItem: ProfileListingModel,
            newItem: ProfileListingModel
        ): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }


}