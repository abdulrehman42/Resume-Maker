package com.example.resumemaker.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.os.Bundle
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddAchievementsFRagmentBinding


class AddAchievementsFRagment : BaseFragment<FragmentAddAchievementsFRagmentBinding>()
{
    override val inflate: Inflate<FragmentAddAchievementsFRagmentBinding>
        get() = FragmentAddAchievementsFRagmentBinding::inflate

    override fun observeLiveData() {
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text="Add Achievement"
        val data=sharePref.readDataEducation()
        if (data!=null)
        {
            binding.achieveedittext.setText(data.universityName)
            binding.descriptionedittext.setText(data.degree)
            binding.issueDateeedittext.setText(data.startDate+data.endDate)

        }
        onclick()

    }



    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.savebtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }

    }


}