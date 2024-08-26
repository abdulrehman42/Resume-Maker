package com.pentabit.cvmaker.resumebuilder.views.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.databinding.CustomprofileitemBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileListingModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxChooseProfile
import com.pentabit.cvmaker.resumebuilder.utils.Helper.saveHtmlAsPdf

class ProfileAdapter(
    val context: Activity, val list: List<ProfileListingModel>,
    val onclick:(Int)->Unit,
    val onDelete:(Int)->Unit,
    val onEdit:(Int)->Unit): RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: CustomprofileitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: ProfileListingModel) {
            binding.name.text = model.name.replaceFirstChar { it.uppercase() }
            binding.profession.text=model.jobTitle
            Glide.with(binding.imageProfile.context).load(com.pentabit.cvmaker.resumebuilder.utils.Constants.BASE_MEDIA_URL+model.path).into(binding.imageProfile)
            binding.textView4.setOnClickListener {
                onclick(model.id)
            }
            binding.settingMenu.setOnClickListener {
                alertboxChooseProfile(context,
                    object : DialogueBoxes.StringValueDialogCallback {
                        override fun onButtonClick(value: String) {
                            if (value== Constants.EDIT)
                            {
                                onEdit(model.id)
                            }else{
                                onDelete(model.id)
                            }
                        }

                    })
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