package com.example.resumemaker.views.fragments.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentProfileDetailBinding
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.viewmodels.ProfileVM
import com.example.resumemaker.views.activities.AddDetailResume
import com.example.resumemaker.views.adapter.adddetailresume.EducationAdapter
import com.example.resumemaker.views.adapter.adddetailresume.ExperienceProfAdapter
import com.example.resumemaker.views.adapter.SkillProfAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDetailFragment : BaseFragment<FragmentProfileDetailBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    override val inflate: Inflate<FragmentProfileDetailBinding>
        get() = FragmentProfileDetailBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(currentActivity()) {
            sharePref.writeDataProfile(it)
            setValues(it)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(currentActivity())[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text = getString(R.string.user_profile)
        binding.includeTool.share.setImageResource(R.drawable.baseline_edit_24)
        apiCAll()
        onclick()

    }

    private fun apiCAll() {
        addDetailResumeVM.getProfileDetail(sharePref.readString(Constants.PROFILE_ID).toString())
    }

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.includeTool.share.setOnClickListener {
            currentActivity().startActivity(Intent(currentActivity(), AddDetailResume::class.java))
        }
    }

    private fun setValues(profileModelAddDetailResponse: ProfileModelAddDetailResponse) {
        val skillAdapter =
            SkillProfAdapter(currentActivity(), profileModelAddDetailResponse.userSkills)
        val experienceAdapter =
            ExperienceProfAdapter(currentActivity(), profileModelAddDetailResponse.userExperiences)
        val eduAdapter = EducationAdapter(
            currentActivity(),
            profileModelAddDetailResponse.userQualifications,
            true
        )
        {}
        Glide.with(currentActivity()).load(Constants.BASE_MEDIA_URL+profileModelAddDetailResponse.path).into(binding.shapeableImageView)
        binding.scrollview.isSmoothScrollingEnabled = true
        binding.apply {
            userName.text = profileModelAddDetailResponse.name
            userIntro.text = profileModelAddDetailResponse.objective
            gender.text = profileModelAddDetailResponse.gender
            location.text = profileModelAddDetailResponse.address
            objexttext.text = profileModelAddDetailResponse.objective
            skillRecyclerview.apply {
                layoutManager = GridLayoutManager(currentActivity(), 3)
            }
            experienceRecyclerview.adapter = experienceAdapter
            skillRecyclerview.adapter = skillAdapter
            educationRecyclerview.adapter = eduAdapter
        }
    }

}