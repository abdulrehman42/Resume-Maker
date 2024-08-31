package com.pentabit.cvmaker.resumebuilder.views.fragments.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentProfileDetailBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.activities.ChoiceTemplate
import com.pentabit.cvmaker.resumebuilder.views.adapter.ProfileEduAdapter
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.EducationAdapter
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ExperienceProfAdapter
import com.pentabit.cvmaker.resumebuilder.views.adapter.SkillProfAdapter
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDetailFragment : BaseFragment<FragmentProfileDetailBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    override val inflate: Inflate<FragmentProfileDetailBinding>
        get() = FragmentProfileDetailBinding::inflate
    var isclick = false

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(currentActivity()) {
            setValues(it)
        }
        addDetailResumeVM.loadingState.observe(currentActivity()) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)

            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
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
        addDetailResumeVM.getProfileDetail()

    }

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.includeTool.share.setOnClickListener {
            val intent = Intent(requireActivity(), AddDetailResume::class.java)
            intent.putExtra(Constants.IS_EDIT, true)
            startActivity(intent)
        }

        binding.createProfile.setOnClickListener {
            startActivity(Intent(currentActivity(), AddDetailResume::class.java))
        }
        binding.createCoverletter.setOnClickListener {
            val intent = Intent(currentActivity(), ChoiceTemplate::class.java)
            intent.putExtra(Constants.IS_RESUME, false)
            startActivity(intent)
        }
        binding.createBtn.setOnClickListener {
            binding.createProfile.isGone = false
            binding.createCoverletter.isGone = false
        }
        binding.createBtn.setOnClickListener {
            if (isclick) {
                openButton(isclick)
            } else {
                openButton(isclick)

            }
        }
    }

    fun openButton(isclick: Boolean) {

        binding.createProfile.isGone = isclick
        binding.createCoverletter.isGone = isclick
        this.isclick = !isclick
    }


    private fun setValues(profileModelAddDetailResponse: ProfileModelAddDetailResponse) {
        val skillAdapter =
            SkillProfAdapter(currentActivity(), profileModelAddDetailResponse.userSkills)
        val experienceAdapter =
            ExperienceProfAdapter(currentActivity(), profileModelAddDetailResponse.userExperiences)
        val eduAdapter = ProfileEduAdapter()
        eduAdapter.submitList(profileModelAddDetailResponse.userQualifications)
        Glide.with(currentActivity())
            .load(Constants.BASE_MEDIA_URL + profileModelAddDetailResponse.path)
            .placeholder(R.drawable.placeholder_image).into(binding.shapeableImageView)
        binding.scrollview.isSmoothScrollingEnabled = true
        binding.apply {
            userName.text = profileModelAddDetailResponse.name
            binding.userIntro.setText(profileModelAddDetailResponse.jobTitle)
            gender.text = profileModelAddDetailResponse.gender.replaceFirstChar { it.uppercase() }

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