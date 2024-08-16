package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentResumePreviewBinding
import com.example.resumemaker.utils.DialogueBoxes.alertboxChooseDownload
import com.example.resumemaker.utils.DialogueBoxes.shareAppMethod

class ResumePreviewFragment : BaseFragment<FragmentResumePreviewBinding>() {
    override val inflate: Inflate<FragmentResumePreviewBinding>
        get() = FragmentResumePreviewBinding::inflate

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text="Preview"
        onclick()
    }

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.includeTool.share.setOnClickListener {
            shareAppMethod(currentActivity())
        }
        binding.downloadConstraint.setOnClickListener{
            alertboxChooseDownload(currentActivity())
        }

    }

}
